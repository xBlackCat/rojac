package org.xblackcat.sunaj.service.storage.cached;

import org.xblackcat.sunaj.data.Forum;
import org.xblackcat.sunaj.service.storage.IForumAH;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */
final class CachedForumAH implements IForumAH, IPurgable {
    private final Cache<Forum> forumCache = new Cache<Forum>();
    private final ObjectCache<int[]> subscribedForums = new ObjectCache<int[]>();

    private final IForumAH forumAH;

    CachedForumAH(IForumAH forumAH) {
        this.forumAH = forumAH;
    }

    public void storeForum(Forum f) throws StorageException {
        forumAH.storeForum(f);
    }

    public boolean removeForum(int id) throws StorageException {
        if (forumAH.removeForum(id)) {
            forumCache.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public Forum getForumById(int forumId) throws StorageException {
        Forum forum = forumCache.get(forumId);
        if (forum == null) {
            forum = forumAH.getForumById(forumId);
            if (forum != null) {
                forumCache.put(forumId, forum);
            }
        }
        return forum;
    }

    public int[] getForumIdsInGroup(int forumGroupId) throws StorageException {
        return forumAH.getForumIdsInGroup(forumGroupId);
    }

    public Forum[] getAllForums() throws StorageException {
        return forumAH.getAllForums();
    }

    public int[] getSubscribedForumIds() throws StorageException {
        int[] ids = subscribedForums.get();
        if (ids == null) {
            ids = forumAH.getSubscribedForumIds();
            if (ids != null) {
                subscribedForums.put(ids);
            }
        }
        return ids;
    }

    public void updateForum(Forum f) throws StorageException {
        forumAH.updateForum(f);
    }

    public void setSubscribeForum(int forumId, boolean subscribe) throws StorageException {
        forumAH.setSubscribeForum(forumId, subscribe);
    }

    public int getMessagesInForum(int forumId) throws StorageException {
        return forumAH.getMessagesInForum(forumId);
    }

    public void purge() {
        forumCache.purge();
        subscribedForums.purge();
    }
}
