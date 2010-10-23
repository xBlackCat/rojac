package org.xblackcat.rojac.service.janus.commands;

import gnu.trove.TIntHashSet;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.service.janus.data.TopicMessages;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.*;
import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author xBlackCat
 */

abstract class ALoadPostsRequest extends ARequest<ProcessPacket> {
    protected final IStorage storage;
    protected final IRatingAH rAH;
    protected final IMessageAH mAH;
    protected final IModerateAH modAH;
    protected final IMiscAH miscAH;
    protected final IForumAH forumAH;

    private final TIntHashSet updatedTopics = new TIntHashSet();

    public ALoadPostsRequest() {
        storage = ServiceFactory.getInstance().getStorage();
        modAH = storage.getModerateAH();
        mAH = storage.getMessageAH();
        rAH = storage.getRatingAH();
        miscAH = storage.getMiscAH();
        forumAH = storage.getForumAH();
    }

    protected Collection<AffectedMessage> storeNewPosts(IProgressTracker tracker, TopicMessages newPosts) throws StorageException {
        tracker.addLodMessage(Messages.Synchronize_Command_UpdateDatabase);
        Set<AffectedMessage> result = new HashSet<AffectedMessage>();

        TIntHashSet updatedTopics = new TIntHashSet();
        int count = 0;
        for (JanusMessageInfo mes : newPosts.getMessages()) {
            tracker.updateProgress(count++, newPosts.getTotalRecords());

            // TODO: compute the flag depending on MessageData and user settings.
            boolean read = false;
            if (Property.SYNCHRONIZER_MARK_MY_POST_READ.get() && Property.RSDN_USER_ID.isSet()) {
                read = mes.getUserId() == Property.RSDN_USER_ID.get();
            }

            int mId = mes.getMessageId();
            if (mes.getTopicId() == 0) {
                updatedTopics.add(mes.getMessageId());
            } else {
                updatedTopics.add(mes.getTopicId());
            }

            if (mAH.isExist(mId)) {
                mAH.updateMessage(mes, read);
            } else {
                mAH.storeMessage(mes, read);
            }


            int forumId = mes.getForumId();
            result.add(new AffectedMessage(forumId, mId));
        }

        for (JanusModerateInfo mod : newPosts.getModerates()) {
            tracker.updateProgress(count++, newPosts.getTotalRecords());
            modAH.storeModerateInfo(mod);
            result.add(new AffectedMessage(mod.getForumId(), mod.getMessageId()));
        }

        for (JanusRatingInfo r : newPosts.getRatings()) {
            tracker.updateProgress(count++, newPosts.getTotalRecords());
            rAH.storeRating(r);
            result.add(new AffectedMessage(AffectedMessage.DEFAULT_FORUM, r.getMessageId()));
        }

        return result;
    }
}
