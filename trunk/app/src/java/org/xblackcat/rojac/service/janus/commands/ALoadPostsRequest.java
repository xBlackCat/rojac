package org.xblackcat.rojac.service.janus.commands;

import gnu.trove.TIntHashSet;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.ForumsUpdatePacket;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.PostsUpdatePacket;
import org.xblackcat.rojac.service.datahandler.ThreadsUpdatePacket;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.*;
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

    public ALoadPostsRequest() {
        storage = ServiceFactory.getInstance().getStorage();
        modAH = storage.getModerateAH();
        mAH = storage.getMessageAH();
        rAH = storage.getRatingAH();
        miscAH = storage.getMiscAH();
        forumAH = storage.getForumAH();
    }

    protected void storeNewPosts(IProgressTracker tracker, TopicMessages newPosts) throws StorageException {
        tracker.addLodMessage(Messages.Synchronize_Command_UpdateDatabase);

        int count = 0;
        for (JanusMessageInfo mes : newPosts.getMessages()) {
            tracker.updateProgress(count++, newPosts.getTotalRecords());

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

        for (JanusModerateInfo mod : newPosts.getModerates()) {
            tracker.updateProgress(count++, newPosts.getTotalRecords());
            modAH.storeModerateInfo(mod);
            updatedForums.add(mod.getForumId());
            updatedMessages.add(mod.getMessageId());
        }

        for (JanusRatingInfo r : newPosts.getRatings()) {
            tracker.updateProgress(count++, newPosts.getTotalRecords());
            rAH.storeRating(r);
            updatedMessages.add(r.getMessageId());
        }
    }

    protected void setNotifications(IResultHandler<IPacket> handler) {
        handler.process(new ForumsUpdatePacket(updatedForums.toArray()));
        handler.process(new ThreadsUpdatePacket(updatedTopics.toArray()));
        handler.process(new PostsUpdatePacket(updatedMessages.toArray()));
    }
}
