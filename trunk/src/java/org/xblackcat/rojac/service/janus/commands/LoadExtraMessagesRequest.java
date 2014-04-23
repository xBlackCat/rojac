package org.xblackcat.rojac.service.janus.commands;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;
import org.apache.commons.lang3.ArrayUtils;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.Role;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.SynchronizationCompletePacket;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.janus.data.UsersList;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.*;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.sjpu.storage.IBatch;
import org.xblackcat.sjpu.storage.StorageException;
import ru.rsdn.janus.JanusMessageInfo;
import ru.rsdn.janus.JanusModerateInfo;
import ru.rsdn.janus.JanusRatingInfo;

import java.util.Arrays;
import java.util.List;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_ID;

/**
 * Command for loading extra and broken messages.
 *
 * @author xBlackCat
 */

class LoadExtraMessagesRequest extends ARequest<IPacket> {
    protected final IRatingAH rAH;
    protected final IMessageAH mAH;
    protected final IModerateAH modAH;
    protected final IMiscAH miscAH;
    protected final IForumAH forumAH;
    protected final IUserAH userAH;

    private final TIntHashSet updatedTopics = new TIntHashSet();
    private final TIntHashSet updatedForums = new TIntHashSet();
    private final TIntHashSet updatedMessages = new TIntHashSet();
    private final TIntHashSet newPostsMessages = new TIntHashSet();

    private final TIntHashSet ratingCacheUpdate = new TIntHashSet();
    private final TIntObjectHashMap<String> nonExistUsers = new TIntObjectHashMap<>();
    private final TIntHashSet existUsers = new TIntHashSet();
    private final TIntHashSet nonExistRatingUsers = new TIntHashSet();

    LoadExtraMessagesRequest() {
        modAH = Storage.get(IModerateAH.class);
        mAH = Storage.get(IMessageAH.class);
        rAH = Storage.get(IRatingAH.class);
        miscAH = Storage.get(IMiscAH.class);
        forumAH = Storage.get(IForumAH.class);
        userAH = Storage.get(IUserAH.class);
    }

    public void process(
            IResultHandler<IPacket> handler,
            ILogTracker tracker,
            IJanusService janusService
    ) throws RojacException {
        try {
            int ownUserId = loadData(tracker, janusService);

            if (ownUserId > 0) {
                RSDN_USER_ID.set(ownUserId);
                tracker.addLodMessage(Message.Synchronize_Message_GotUserId, ownUserId);
            }

            postProcessing(tracker, janusService);

            // Do not update zero-identified objects
            updatedForums.remove(0);
            updatedTopics.remove(0);
            updatedMessages.remove(0);

            handler.process(new SynchronizationCompletePacket(updatedForums, updatedTopics, updatedMessages));
        } catch (StorageException e) {
            throw new RojacException(e.getMessage(), e);
        }
    }

    protected int loadData(
            ILogTracker tracker,
            IJanusService janusService
    ) throws StorageException, RsdnProcessorException {
        int portion = Property.SYNCHRONIZER_LOAD_TOPICS_PORTION.get();

        try (IBatch batch = Storage.startBatch()) {
            IMiscAH miscAH = batch.get(IMiscAH.class);
            IMessageAH mAH = batch.get(IMessageAH.class);

            int[] messageIds = miscAH.getExtraMessages();

            try {
                if (ArrayUtils.isNotEmpty(messageIds)) {
                    int offset = 0;
                    while (offset < messageIds.length) {
                        final int[] portionIds = ArrayUtils.subarray(messageIds, offset, offset + portion);

                        tracker.addLodMessage(Message.Synchronize_Command_Name_ExtraPosts, Arrays.toString(portionIds));

                        storeNewPosts(batch, tracker, janusService.getTopicByMessage(portionIds));

                        offset += portionIds.length;
                    }

                    miscAH.clearExtraMessages();
                }

                int[] brokenTopicIds = mAH.getBrokenTopicIds();
                if (ArrayUtils.isNotEmpty(brokenTopicIds)) {
                    int offset = 0;
                    while (offset < brokenTopicIds.length) {
                        final int[] portionIds = ArrayUtils.subarray(brokenTopicIds, offset, offset + portion);

                        tracker.addLodMessage(
                                Message.Synchronize_Command_Name_BrokenTopics,
                                Arrays.toString(portionIds)
                        );

                        storeNewPosts(batch, tracker, janusService.getTopicByMessage(portionIds));

                        offset += portionIds.length;
                    }
                }

                batch.commit();
            } catch (JanusServiceException e) {
                throw new RsdnProcessorException("Can not load extra messages.", e);
            }
        }

        return 0;
    }

    protected void storeNewPosts(ILogTracker tracker, TopicMessages newPosts) throws StorageException {
        try (IBatch batch = Storage.startBatch()) {
            boolean done = false;
            try {
                storeNewPosts(batch, tracker, newPosts);
                done = true;
            } finally {
                if (done) {
                    batch.commit();
                } else {
                    batch.rollback();
                }
            }
        }
    }

    private void storeNewPosts(IBatch batch, ILogTracker tracker, TopicMessages newPosts) throws StorageException {
        IModerateAH modAH = batch.get(IModerateAH.class);
        IMessageAH mAH = batch.get(IMessageAH.class);
        IRatingAH rAH = batch.get(IRatingAH.class);

        List<JanusMessageInfo> messages = newPosts.getMessages();
        List<JanusModerateInfo> moderates = newPosts.getModerates();
        List<JanusRatingInfo> ratings = newPosts.getRatings();

        int messagesAmount = messages.size();
        int moderatesAmount = moderates.size();
        int ratings1Amount = ratings.size();

        tracker.addLodMessage(Message.Synchronize_Message_GotPosts, messagesAmount, moderatesAmount, ratings1Amount);

        tracker.addLodMessage(Message.Synchronize_Message_UpdateDatabase);

        tracker.addLodMessage(Message.Synchronize_Message_StoreMessages);
        int count = 0;
        for (JanusMessageInfo mes : messages) {
            tracker.updateProgress(count++, messagesAmount);

            boolean read = shouldTheMessageRead(mes);

            int mId = mes.getMessageId();
            if (mAH.isExist(mId)) {
                mAH.updateMessage(
                        mes.getTopicId(),
                        mes.getParentId(),
                        mes.getUserId(),
                        mes.getForumId(),
                        mes.getArticleId(),
                        mes.getUserTitleColor(),
                        Role.getUserType(mes.getUserRole()),
                        mes.getMessageDate().toGregorianCalendar().getTimeInMillis(),
                        mes.getUpdateDate().toGregorianCalendar().getTimeInMillis(),
                        mes.getLastModerated().toGregorianCalendar().getTimeInMillis(),
                        mes.getSubject(),
                        mes.getMessageName(),
                        mes.getUserNick(),
                        mes.getUserTitle(),
                        mes.getMessage(),
                        read, mes.getMessageId()
                );
                updatedMessages.add(mId);
            } else {
                mAH.storeMessage(
                        mes.getMessageId(),
                        mes.getTopicId(),
                        mes.getParentId(),
                        mes.getUserId(),
                        mes.getForumId(),
                        mes.getArticleId(),
                        mes.getUserTitleColor(),
                        Role.getUserType(mes.getUserRole()),
                        mes.getMessageDate().toGregorianCalendar().getTimeInMillis(),
                        mes.getUpdateDate().toGregorianCalendar().getTimeInMillis(),
                        mes.getLastModerated().toGregorianCalendar().getTimeInMillis(),
                        mes.getSubject(),
                        mes.getMessageName(),
                        mes.getUserNick(),
                        mes.getUserTitle(),
                        mes.getMessage(),
                        read
                );
                newPostsMessages.add(mId);
            }

            if (mes.getTopicId() == 0) {
                updatedTopics.add(mes.getMessageId());
            } else {
                updatedTopics.add(mes.getTopicId());
            }

            updatedForums.add(mes.getForumId());

            int userId = mes.getUserId();
            // Check user info.

            if (existUsers.contains(userId)) {
                // The user is already exists in DB.
                continue;
            }

            // Do not check DB if user already queued for storing
            if (nonExistUsers.containsKey(userId)) {
                continue;
            }

            // User is not stored in caches - check database
            if (!userAH.isUserExists(userId)) {
                // User not exists - queue for storing
                nonExistUsers.put(userId, mes.getUserNick());
            } else {
                existUsers.add(userId);
            }
        }

        tracker.addLodMessage(Message.Synchronize_Message_StoreModerates);
        count = 0;
        for (JanusModerateInfo mod : moderates) {
            tracker.updateProgress(count++, moderatesAmount);

            modAH.storeModerateInfo(
                    mod.getMessageId(),
                    mod.getUserId(),
                    mod.getForumId(),
                    mod.getCreate().toGregorianCalendar().getTimeInMillis()
            );
            updatedForums.add(mod.getForumId());
            updatedMessages.add(mod.getMessageId());
        }

        tracker.addLodMessage(Message.Synchronize_Message_StoreRatings);
        count = 0;
        for (JanusRatingInfo r : ratings) {
            tracker.updateProgress(count++, ratings1Amount);

            rAH.storeRating(
                    r.getMessageId(), r.getTopicId(), r.getUserId(), r.getUserRating(), Mark.getMark(r.getRate()),
                    r.getRateDate().toGregorianCalendar().getTimeInMillis()
            );
            updatedMessages.add(r.getMessageId());
            ratingCacheUpdate.add(r.getMessageId());

            int userId = r.getUserId();

            if (existUsers.contains(userId)) {
                // The user is already exists in DB.
                continue;
            }

            // Do not check DB if user already queued for storing
            if (nonExistUsers.containsKey(userId)) {
                continue;
            }

            // User is not stored in caches - check database
            if (userAH.isUserExists(userId)) {
                existUsers.add(userId);
            } else {
                // Try to load users from JanusAT
                nonExistRatingUsers.add(userId);
            }
        }
    }

    private boolean shouldTheMessageRead(JanusMessageInfo mes) {
        if (Property.SYNCHRONIZER_MARK_MY_POST_READ.get() && Property.RSDN_USER_ID.isSet()) {
            if (mes.getUserId() == Property.RSDN_USER_ID.get()) {
                // Own posts mark as read
                return true;
            }
        }

        // TODO: compute the flag depending on MessageData and user settings.

        return false;
    }

    protected void postProcessing(
            ILogTracker tracker,
            IJanusService janusService
    ) throws StorageException, RsdnProcessorException {
        if (!nonExistUsers.isEmpty() || !nonExistRatingUsers.isEmpty()) {
            int[] userIds;
            if (Property.SYNCHRONIZER_LOAD_USERS.get()) {
                TIntHashSet usersToLoad = new TIntHashSet(nonExistUsers.keys());
                usersToLoad.addAll(nonExistRatingUsers);

                // Try to loads users from JanusAT
                userIds = usersToLoad.toArray();
                try (IBatch batch = Storage.startBatch()) {
                    IUserAH userAH = batch.get(IUserAH.class);
                    try {
                        int portion = Property.SYNCHRONIZER_LOAD_USERS_PORTION.get();
                        int offset = 0;

                        while (offset < userIds.length) {
                            int[] portionIds = ArrayUtils.subarray(userIds, offset, offset + portion);
                            tracker.addLodMessage(Message.Synchronize_Command_Name_Users, Arrays.toString(portionIds));

                            UsersList usersByIds = janusService.getUsersByIds(portionIds);

                            tracker.addLodMessage(Message.Synchronize_Message_StoreFullUserInfo);
                            User[] users = usersByIds.getUsers();
                            for (int i = 0, usersLength = users.length; i < usersLength; i++) {
                                User user = users[i];
                                userAH.storeUser(
                                        user.getId(), user.getUserName(), user.getUserNick(), user.getRealName(),
                                        user.getPublicEmail(), user.getHomePage(), user.getSpecialization(), user.getWhereFrom(),
                                        user.getOrigin()
                                );
                                nonExistUsers.remove(user.getId());
                                nonExistRatingUsers.remove(user.getId());
                                tracker.updateProgress(i, usersLength);
                            }

                            offset += portionIds.length;
                        }

                        // If we still have unresolved users?
                        // Process only users we could resolve names from posts
                        if (!nonExistUsers.isEmpty()) {
                            userIds = nonExistUsers.keys();
                            tracker.addLodMessage(Message.Synchronize_Message_StoreUserInfo);
                            for (int i = 0, userIdsLength = userIds.length; i < userIdsLength; i++) {
                                int userId = userIds[i];
                                final String userName = nonExistUsers.get(userId);
                                userAH.storeUser(userId, userName, userName, null, null, null, null, null, null);
                                tracker.updateProgress(i, userIdsLength);
                            }
                        }

                        batch.commit();
                    } catch (JanusServiceException e) {
                        tracker.postException(e);
                        batch.rollback();
                    }
                }
            }
        }

        int idx = 0;
        int total = ratingCacheUpdate.size();
        tracker.addLodMessage(Message.Synchronize_Message_UpdateCaches);
        try (IBatch batch = Storage.startBatch()) {
            try {
                for (int anInt : ratingCacheUpdate.toArray()) {
                    MessageUtils.updateRatingCache(batch, anInt);
                    tracker.updateProgress(idx++, total);
                }

                IMessageAH mAH = batch.get(IMessageAH.class);
                IForumAH forumAH = batch.get(IForumAH.class);

                final BatchTracker batchTracker = new BatchTracker(tracker, 3);
                batchTracker.setBatch(0, 1);
                idx = 0;
                total = newPostsMessages.size();
                for (int messageId : newPostsMessages.toArray()) {
                    mAH.updateParentPostUserId(messageId);
                    batchTracker.updateProgress(idx++, total);
                }

                batchTracker.nextSuperBatch();
                idx = 0;
                total = updatedTopics.size();
                for (int topicId : updatedTopics.toArray()) {
                    mAH.updateRepliesAmountInfo(topicId);
                    mAH.updateLastPostId(topicId);
                    mAH.updateLastPostDate(topicId);
                    batchTracker.updateProgress(idx++, total);
                }

                batchTracker.nextSuperBatch();
                idx = 0;
                total = updatedForums.size();
                for (int forumId : updatedForums.toArray()) {
                    forumAH.updateForumStatistic(forumId);
                    batchTracker.updateProgress(idx++, total);
                }

                batch.commit();
            } catch (StorageException e) {
                batch.rollback();
                tracker.postException(e);
            }
        }

    }

}
