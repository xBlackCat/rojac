package org.xblackcat.rojac.service.janus.commands;

import gnu.trove.TIntHashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.storage.*;
import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;

/**
 * @author xBlackCat
 */

abstract class ALoadPostsRequest extends ARequest {
    private static final Log log = LogFactory.getLog(ALoadPostsRequest.class);

    protected final AffectedIds processed = new AffectedIds();

    protected final IStorage storage;
    protected final IRatingAH rAH;
    protected final IMessageAH mAH;
    protected final IModerateAH modAH;
    protected final IMiscAH miscAH;
    protected final IForumAH forumAH;

    private final TIntHashSet loadedMessages = new TIntHashSet();

    public ALoadPostsRequest() {
        storage = ServiceFactory.getInstance().getStorage();
        modAH = storage.getModerateAH();
        mAH = storage.getMessageAH();
        rAH = storage.getRatingAH();
        miscAH = storage.getMiscAH();
        forumAH = storage.getForumAH();
    }

    protected void storeNewPosts(IProgressTracker tracker, TopicMessages newPosts) throws StorageException {
        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_UPDATE_DATABASE);

        int count = 0;
        for (JanusMessageInfo mes : newPosts.getMessages()) {
            tracker.updateProgress(count++, newPosts.getTotalRecords());
            int mId = mes.getMessageId();
            if (mAH.isExist(mId)) {
                mAH.updateMessage(mes);
            } else {
                mAH.storeMessage(mes);
            }

            loadedMessages.add(mId);

            long mesDate = mes.getMessageDate().getTimeInMillis();

            int forumId = mes.getForumId();
            processed.addMessageId(forumId, mId);
        }

        for (JanusModerateInfo mod : newPosts.getModerates()) {
            tracker.updateProgress(count++, newPosts.getTotalRecords());
            modAH.storeModerateInfo(mod);
            processed.addMessageId(mod.getForumId(), mod.getMessageId());
        }
        for (JanusRatingInfo r : newPosts.getRatings()) {
            tracker.updateProgress(count++, newPosts.getTotalRecords());
            rAH.storeRating(r);
            processed.addMessageId(r.getMessageId());
        }
    }
}
