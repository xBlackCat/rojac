package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumGroup;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.ForumsUpdated;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.ForumsList;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IForumGroupAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * @author xBlackCat
 */

class GetForumListRequest extends ARequest<IPacket> {
    public void process(IResultHandler<IPacket> handler, IProgressTracker tracker, IJanusService janusService) throws RojacException {
        tracker.addLodMessage(Message.Synchronize_Command_Name_ForumList);

        ForumsList forumsList;
        try {
            forumsList = janusService.getForumsList(DataHelper.getVersion(VersionType.FORUM_ROW_VERSION));
        } catch (JanusServiceException e) {
            throw new RsdnProcessorException("Can not obtain forums list", e);
        }

        IForumAH fAH = Storage.get(IForumAH.class);
        IForumGroupAH gAH = Storage.get(IForumGroupAH.class);

        tracker.addLodMessage(Message.Synchronize_Message_GotForums, forumsList.getForums().length, forumsList.getForumGroups().length);

        try {
            int total = forumsList.getForumGroups().length + forumsList.getForums().length;

            int count = 0;
            for (ForumGroup fg : forumsList.getForumGroups()) {
                tracker.updateProgress(count++, total);
                if (gAH.getForumGroupById(fg.getForumGroupId()) == null) {
                    gAH.storeForumGroup(fg);
                } else {
                    gAH.updateForumGroup(fg);
                }

            }

            for (Forum f : forumsList.getForums()) {
                tracker.updateProgress(count++, total);
                if (fAH.getForumById(f.getForumId()) == null) {
                    fAH.storeForum(f);
                } else {
                    fAH.updateForum(f);
                }
            }

            DataHelper.setVersion(VersionType.FORUM_ROW_VERSION, forumsList.getVersion());
        } catch (StorageException e) {
            throw new RsdnProcessorException("Can not update forum list", e);
        }

        handler.process(new ForumsUpdated());
    }
}
