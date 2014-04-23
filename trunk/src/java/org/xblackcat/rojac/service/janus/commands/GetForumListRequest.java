package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.*;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.ForumsUpdated;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.ForumsList;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IForumGroupAH;
import org.xblackcat.rojac.service.storage.IVersionAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.sjpu.storage.StorageException;

/**
 * @author xBlackCat
 */

class GetForumListRequest extends ARequest<IPacket> {
    public void process(IResultHandler<IPacket> handler, ILogTracker tracker, IJanusService janusService) throws RojacException {
        tracker.addLodMessage(Message.Synchronize_Command_Name_ForumList);

        ForumsList forumsList;
        try {
            IVersionAH vAH = Storage.get(IVersionAH.class);
            VersionInfo versionInfo = vAH.getVersionInfo(VersionType.FORUM_ROW_VERSION);
            forumsList = janusService.getForumsList(versionInfo == null ? new Version() : versionInfo.getVersion());
        } catch (JanusServiceException e) {
            throw new RsdnProcessorException("Can't obtain forums list", e);
        } catch (StorageException e) {
            throw new RsdnProcessorException("Can't read current version", e);
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
                    gAH.storeForumGroup(fg.getForumGroupId(), fg.getForumGroupName(), fg.getSortOrder());
                } else {
                    gAH.updateForumGroup(fg.getForumGroupName(), fg.getSortOrder(), fg.getForumGroupId());
                }

            }

            for (Forum f : forumsList.getForums()) {
                tracker.updateProgress(count++, total);
                if (fAH.getForumById(f.getForumId()) == null) {
                    fAH.storeForum(
                            f.getForumId(), f.getForumGroupId(), f.getRated(), f.getInTop(), f.getRateLimit(),
                            f.isSubscribed(), f.getShortForumName(), f.getForumName()
                    );
                } else {
                    fAH.updateForum(
                            f.getForumGroupId(), f.getRated(), f.getInTop(), f.getRateLimit(),
                            f.getShortForumName(), f.getForumName(), f.getForumId()
                    );
                }
            }

            IVersionAH vAH = Storage.get(IVersionAH.class);
            final VersionInfo v = new VersionInfo(VersionType.FORUM_ROW_VERSION, forumsList.getVersion());
            vAH.updateVersionInfo(v.getType(), v.getVersion());
        } catch (StorageException e) {
            throw new RsdnProcessorException("Can not update forum list", e);
        }

        handler.process(new ForumsUpdated());
    }
}
