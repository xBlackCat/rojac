package org.xblackcat.rojac.service.storage.cached;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;

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

    public int[] getAllForumIds() throws StorageException {
        return forumAH.getAllForumIds();
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

    public void setForumRead(int forumId, boolean read) throws StorageException {
        forumAH.setForumRead(forumId, read);
    }

    public int getMessagesInForum(int forumId) throws StorageException {
        return forumAH.getMessagesInForum(forumId);
    }

    public int getUnreadMessagesInForum(int forumId) throws StorageException {
        return forumAH.getUnreadMessagesInForum(forumId);
    }

    public void purge() {
        forumCache.purge();
        subscribedForums.purge();
    }
}
