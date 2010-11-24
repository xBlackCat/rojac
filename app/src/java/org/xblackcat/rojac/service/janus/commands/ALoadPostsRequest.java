package org.xblackcat.rojac.service.janus.commands;

import gnu.trove.TIntHashSet;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.SynchronizationCompletePacket;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.*;
import org.xblackcat.rojac.util.MessageUtils;
import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;

/**
 * @author xBlackCat
 */

abstract class ALoadPostsRequest extends ARequest<IPacket> {
    protected final IStorage storage;
    protected final IRatingAH rAH;
    protected final IMessageAH mAH;
    protected final IModerateAH modAH;
    protected final IMiscAH miscAH;
    protected final IForumAH forumAH;

    private final TIntHashSet updatedTopics = new TIntHashSet();
    private final TIntHashSet updatedForums = new TIntHashSet();
    private final TIntHashSet updatedMessages = new TIntHashSet();
    private final TIntHashSet ratingCacheUpdate = new TIntHashSet();

    public ALoadPostsRequest() {
        storage = ServiceFactory.getInstance().getStorage();
        modAH = storage.getModerateAH();
        mAH = storage.getMessageAH();
        rAH = storage.getRatingAH();
        miscAH = storage.getMiscAH();
        forumAH = storage.getForumAH();
    }

    protected void storeNewPosts(IProgressTracker tracker, TopicMessages newPosts) throws StorageException {
        JanusMessageInfo[] messages = newPosts.getMessages();
        JanusModerateInfo[] moderates = newPosts.getModerates();
        JanusRatingInfo[] ratings = newPosts.getRatings();

        tracker.addLodMessage(Messages.Synchronize_Message_GotPosts, messages.length, moderates.length, ratings.length);

        tracker.addLodMessage(Messages.Synchronize_Message_UpdateDatabase);

        tracker.addLodMessage(Messages.Synchronize_Message_StoreMessages);
        int count = 0;
        for (JanusMessageInfo mes : newPosts.getMessages()) {
            tracker.updateProgress(count++, newPosts.getMessages().length);

            // TODO: compute the flag depending on MessageData and user settings.
            boolean read = false;
            if (Property.SYNCHRONIZER_MARK_MY_POST_READ.get() && Property.RSDN_USER_ID.isSet()) {
                read = mes.getUserId() == Property.RSDN_USER_ID.get();
            }

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
        }

        tracker.addLodMessage(Messages.Synchronize_Message_StoreModerates);
        count = 0;
        for (JanusModerateInfo mod : newPosts.getModerates()) {
            tracker.updateProgress(count++, newPosts.getModerates().length);
            modAH.storeModerateInfo(mod);
            updatedForums.add(mod.getForumId());
            updatedMessages.add(mod.getMessageId());
        }

        tracker.addLodMessage(Messages.Synchronize_Message_StoreRatings);
        count = 0;
        for (JanusRatingInfo r : newPosts.getRatings()) {
            tracker.updateProgress(count++, newPosts.getRatings().length);
            rAH.storeRating(r);
            updatedMessages.add(r.getMessageId());
            ratingCacheUpdate.add(r.getMessageId());
        }
    }

    protected void postProcessing(IProgressTracker tracker) throws StorageException {
        int count = 0;
        int[] forUpdate = ratingCacheUpdate.toArray();
        tracker.addLodMessage(Messages.Synchronize_Message_UpdateCaches);
        for (int id : forUpdate) {
            tracker.updateProgress(count++, forUpdate.length);
            MessageUtils.updateRatingCache(id);
        }
    }

    protected void setNotifications(IResultHandler<IPacket> handler) {
        handler.process(new SynchronizationCompletePacket(updatedForums.toArray(), updatedTopics.toArray(), updatedMessages.toArray()));
    }
}
