package org.xblackcat.sunaj.service.storage.cached;

import org.xblackcat.sunaj.data.ForumGroup;
import org.xblackcat.sunaj.service.storage.IForumGroupAH;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 16.04.2007
*
* @author ASUS
*/
final class CachedForumGroupAH implements IForumGroupAH,IPurgable {
    private final Cache<ForumGroup> forumGroupCache = new Cache<ForumGroup>();

    private final IForumGroupAH forumGroupAH;

    CachedForumGroupAH(IForumGroupAH forumGroupAH) {
        this.forumGroupAH = forumGroupAH;
    }

    public void storeForumGroup(ForumGroup fg) throws StorageException {
        forumGroupAH.storeForumGroup(fg);
    }

    public boolean removeForumGroup(int id) throws StorageException {
        if (forumGroupAH.removeForumGroup(id)) {
            forumGroupCache.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public ForumGroup getForumGroupById(int forumGroupId) throws StorageException {
        ForumGroup forumGroup = forumGroupCache.get(forumGroupId);
        if (forumGroup == null) {
            forumGroup = forumGroupAH.getForumGroupById(forumGroupId);
            if (forumGroup != null) {
                forumGroupCache.put(forumGroupId, forumGroup);
            }
        }
        return forumGroup;
    }

    public int[] getAllForumGroupIds() throws StorageException {
        return forumGroupAH.getAllForumGroupIds();
    }

    public void updateForumGroup(ForumGroup fg) throws StorageException {
        forumGroupAH.updateForumGroup(fg);
    }

    public void purge() {
        forumGroupCache.purge();
    }
}
