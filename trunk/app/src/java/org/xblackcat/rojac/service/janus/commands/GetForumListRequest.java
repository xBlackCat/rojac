package org.xblackcat.rojac.service.janus.commands;

import gnu.trove.TIntHashSet;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumGroup;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.ForumsList;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IForumGroupAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * @author xBlackCat
 */

class GetForumListRequest extends ARequest {
    public AffectedIds process(IProgressTracker trac, IJanusService janusService) throws RojacException {
        trac.addLodMessage("Forum list synchronization started.");

        trac.addLodMessage("Load forum list.");

        ForumsList forumsList;
        try {
            forumsList = janusService.getForumsList(RojacHelper.getVersion(VersionType.FORUM_ROW_VERSION));
        } catch (JanusServiceException e) {
            throw new RsdnProcessorException("Can not obtain forums list", e);
        }

        IStorage storage = ServiceFactory.getInstance().getStorage();
        IForumAH fAH = storage.getForumAH();
        IForumGroupAH gAH = storage.getForumGroupAH();

        TIntHashSet updatedForums = new TIntHashSet();
        try {
            trac.addLodMessage("Store forum groups");
            for (ForumGroup fg : forumsList.getForumGroups()) {
                if (gAH.getForumGroupById(fg.getForumGroupId()) == null) {
                    gAH.storeForumGroup(fg);
                } else {
                    gAH.updateForumGroup(fg);
                }

            }

            trac.addLodMessage("Store forum list");
            for (Forum f : forumsList.getForums()) {
                if (fAH.getForumById(f.getForumId()) == null) {
                    fAH.storeForum(f);
                } else {
                    fAH.updateForum(f);
                }
                updatedForums.add(f.getForumId());
            }

            RojacHelper.setVersion(VersionType.FORUM_ROW_VERSION, forumsList.getVersion());

            trac.addLodMessage("Done");
        } catch (StorageException e) {
            throw new RsdnProcessorException("Can not update forum list", e);
        }

        return new AffectedIds(new TIntHashSet(), updatedForums);
    }
}
