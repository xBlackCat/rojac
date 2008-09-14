package org.xblackcat.rojac.service.synchronizer;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumGroup;
import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionInfo;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.gui.frame.progress.IProgressTracker;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.ForumsList;
import org.xblackcat.rojac.service.options.IOptionsService;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IForumGroupAH;
import org.xblackcat.rojac.service.storage.IVersionAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Date: 14 вер 2008
 *
 * @author xBlackCat
 */

public class GetForumListCommand extends ARsdnCommand {
    public void doTask(IProgressTracker trac) throws Exception {
        trac.addLodMessage("Forum list synchronization started.");

        trac.setProgress(0, 3);

        IOptionsService os = ServiceFactory.getInstance().getOptionsService();

        IVersionAH vAH = storage.getVersionAH();

        VersionInfo vi;
        try {
            vi = vAH.getVersionInfo(VersionType.FORUM_ROW_VERSION);

            if (vi == null) {
                vi = new VersionInfo(new Version(), VersionType.FORUM_ROW_VERSION);
            }
        } catch (StorageException e) {
            throw new SynchronizationException("Can not obtain last forum version", e);
        }

        trac.addLodMessage("Load forum list.");
        trac.setProgress(1, 3);

        ForumsList forumsList;
        try {
            forumsList = janusService.getForumsList(vi.getVersion());
        } catch (JanusServiceException e) {
            throw new SynchronizationException("Can not obtain forums list", e);
        }

        IForumAH fAH = storage.getForumAH();
        IForumGroupAH gAH = storage.getForumGroupAH();

        try {
            trac.addLodMessage("Store forum groups");
            int i = 0;
            for (ForumGroup fg : forumsList.getForumGroups()) {
                if (gAH.getForumGroupById(fg.getForumGroupId()) == null) {
                    gAH.storeForumGroup(fg);
                } else {
                    gAH.updateForumGroup(fg);
                }

                trac.setProgress(i++, forumsList.getForumGroups().length);
            }

            trac.addLodMessage("Store forum list");
            i = 0;
            for (Forum f : forumsList.getForums()) {
                if (fAH.getForumById(f.getForumId()) == null) {
                    fAH.storeForum(f);
                } else {
                    fAH.updateForum(f);
                }
                trac.setProgress(i++, forumsList.getForums().length);
            }

            storage.getVersionAH().updateVersionInfo(new VersionInfo(forumsList.getVersion(), VersionType.FORUM_ROW_VERSION));
            trac.addLodMessage("Done");
        } catch (StorageException e) {
            throw new SynchronizationException("Can not update forum list", e);
        }
    }
}
