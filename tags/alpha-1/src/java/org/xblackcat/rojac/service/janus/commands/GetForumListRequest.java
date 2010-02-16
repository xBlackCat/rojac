package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumGroup;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.janus.IJanusService;
import org.xblackcat.rojac.service.janus.JanusServiceException;
import org.xblackcat.rojac.service.janus.data.ForumsList;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IForumGroupAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xBlackCat
 */

class GetForumListRequest extends ARequest {
    public AffectedMessage[] process(IProgressTracker tracker, IJanusService janusService) throws RojacException {
        Set<AffectedMessage> result = new HashSet<AffectedMessage>();

        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_NAME_FORUM_LIST);

        ForumsList forumsList;
        try {
            forumsList = janusService.getForumsList(RojacHelper.getVersion(VersionType.FORUM_ROW_VERSION));
        } catch (JanusServiceException e) {
            throw new RsdnProcessorException("Can not obtain forums list", e);
        }

        IStorage storage = ServiceFactory.getInstance().getStorage();
        IForumAH fAH = storage.getForumAH();
        IForumGroupAH gAH = storage.getForumGroupAH();

        tracker.addLodMessage(Messages.SYNCHRONIZE_COMMAND_GOT_FORUMS, forumsList.getForums().length, forumsList.getForumGroups().length);

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
                result.add(new AffectedMessage(f.getForumId()));
            }

            RojacHelper.setVersion(VersionType.FORUM_ROW_VERSION, forumsList.getVersion());
        } catch (StorageException e) {
            throw new RsdnProcessorException("Can not update forum list", e);
        }

        return result.toArray(new AffectedMessage[result.size()]);
    }
}
