package org.xblackcat.rojac.service.janus.commands;

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;
import org.apache.commons.lang3.ArrayUtils;
import org.xblackcat.rojac.RojacException;
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
import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;

import java.util.Arrays;

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

    public void process(IResultHandler<IPacket> handler, IProgressTracker tracker, IJanusService janusService) throws RojacException {
        int ownUserId = loadData(tracker, janusService);

        if (ownUserId > 0) {
            RSDN_USER_ID.set(ownUserId);
            tracker.addLodMessage(Message.Synchronize_Message_GotUserId, ownUserId);
        }

        postProcessing(tracker, janusService);

        handler.process(new SynchronizationCompletePacket(updatedForums, updatedTopics, updatedMessages));
    }

    protected int loadData(IProgressTracker tracker, IJanusService janusService) throws StorageException, RsdnProcessorException {
        int portion = Property.SYNCHRONIZER_LOAD_TOPICS_PORTION.get();

        int[] messageIds = miscAH.getExtraMessages();

        if (ArrayUtils.isNotEmpty(messageIds)) {
            int offset = 0;
            while (offset < messageIds.length) {
                final int[] portionIds = ArrayUtils.subarray(messageIds, offset, portion);

                tracker.addLodMessage(Message.Synchronize_Command_Name_ExtraPosts, Arrays.toString(portionIds));
                loadTopics(portionIds, janusService, tracker);

                offset += portionIds.length;
            }

            miscAH.clearExtraMessages();
        }

        int[] brokenTopicIds = mAH.getBrokenTopicIds();
        if (ArrayUtils.isNotEmpty(brokenTopicIds)) {
            int offset = 0;
            while (offset < brokenTopicIds.length) {
                final int[] portionIds = ArrayUtils.subarray(brokenTopicIds, offset, portion);

                tracker.addLodMessage(Message.Synchronize_Command_Name_BrokenTopics, Arrays.toString(portionIds));
                loadTopics(portionIds, janusService, tracker);

                offset += portionIds.length;
            }
        }

        return 0;
    }

    private void loadTopics(int[] messageIds, IJanusService janusService, IProgressTracker tracker) throws RsdnProcessorException, StorageException {
        TopicMessages extra;
        try {
            extra = janusService.getTopicByMessage(messageIds);
        } catch (JanusServiceException e) {
            throw new RsdnProcessorException("Can not load extra messages.", e);
        }

        storeNewPosts(tracker, extra);
    }

    protected void storeNewPosts(IProgressTracker tracker, TopicMessages newPosts) throws StorageException {
        JanusMessageInfo[] messages = newPosts.getMessages();
        JanusModerateInfo[] moderates = newPosts.getModerates();
        JanusRatingInfo[] ratings = newPosts.getRatings();

        tracker.addLodMessage(Message.Synchronize_Message_GotPosts, messages.length, moderates.length, ratings.length);

        tracker.addLodMessage(Message.Synchronize_Message_UpdateDatabase);

        tracker.addLodMessage(Message.Synchronize_Message_StoreMessages);
        int count = 0;
        for (JanusMessageInfo mes : newPosts.getMessages()) {
            tracker.updateProgress(count++, newPosts.getMessages().length);

            boolean read = setMessageRead(mes);

            int mId = mes.getMessageId();
            if (mAH.isExist(mId)) {
                mAH.updateMessage(mes, read);
            } else {
                mAH.storeMessage(mes, read);
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
        for (int i = 0, moderatesLength = moderates.length; i < moderatesLength; i++) {
            JanusModerateInfo mod = moderates[i];
            tracker.updateProgress(i, moderatesLength);

            modAH.storeModerateInfo(mod);
            updatedForums.add(mod.getForumId());
            updatedMessages.add(mod.getMessageId());
        }

        tracker.addLodMessage(Message.Synchronize_Message_StoreRatings);
        for (int i = 0, ratings1Length = ratings.length; i < ratings1Length; i++) {
            JanusRatingInfo r = ratings[i];
            tracker.updateProgress(i, ratings1Length);

            rAH.storeRating(r);
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

    private boolean setMessageRead(JanusMessageInfo mes) {
        if (Property.SYNCHRONIZER_MARK_MY_POST_READ.get() && Property.RSDN_USER_ID.isSet()) {
            if (mes.getUserId() == Property.RSDN_USER_ID.get()) {
                // Own posts mark as read
                return true;
            }
        }

        // TODO: compute the flag depending on MessageData and user settings.

        return false;
    }

    protected void postProcessing(IProgressTracker tracker, IJanusService janusService) throws StorageException, RsdnProcessorException {
        if (!nonExistUsers.isEmpty() || !nonExistRatingUsers.isEmpty()) {
            int[] userIds;
            if (Property.SYNCHRONIZER_LOAD_USERS.get()) {
                TIntHashSet usersToLoad = new TIntHashSet(nonExistUsers.keys());
                usersToLoad.addAll(nonExistRatingUsers);

                // Try to loads users from JanusAT
                userIds = usersToLoad.toArray();
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
                            userAH.storeUser(user);
                            nonExistUsers.remove(user.getId());
                            nonExistRatingUsers.remove(user.getId());
                            tracker.updateProgress(i, usersLength);
                        }

                        offset += portionIds.length;
                    }
                } catch (JanusServiceException e) {
                    tracker.postException(e);
                }
            }

            // If we still have unresolved users?
            // Process only users we could resolve names from posts
            if (!nonExistUsers.isEmpty()) {
                userIds = nonExistUsers.keys();
                tracker.addLodMessage(Message.Synchronize_Message_StoreUserInfo);
                for (int i = 0, userIdsLength = userIds.length; i < userIdsLength; i++) {
                    int userId = userIds[i];
                    userAH.storeUserInfo(userId, nonExistUsers.get(userId));
                    tracker.updateProgress(i, userIdsLength);
                }
            }
        }

        int[] forUpdate = ratingCacheUpdate.toArray();
        tracker.addLodMessage(Message.Synchronize_Message_UpdateCaches);
        for (int i = 0, forUpdateLength = forUpdate.length; i < forUpdateLength; i++) {
            MessageUtils.updateRatingCache(forUpdate[i]);
            tracker.updateProgress(i, forUpdateLength);
        }

        mAH.updateLastPostInfo(updatedTopics.toArray());
    }

}
