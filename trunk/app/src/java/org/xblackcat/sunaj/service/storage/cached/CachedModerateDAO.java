package org.xblackcat.sunaj.service.storage.cached;

import org.xblackcat.sunaj.service.data.ModerateInfo;
import org.xblackcat.sunaj.service.storage.IModerateDAO;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */
final class CachedModerateDAO implements IModerateDAO,IPurgable {
    private final Cache<ModerateInfo> moderateCache = new Cache<ModerateInfo>();
    private final Cache<int[]> messageModaratesCache = new Cache<int[]>();

    private final IModerateDAO moderateDAO;

    CachedModerateDAO(IModerateDAO moderateDAO) {
        this.moderateDAO = moderateDAO;
    }

    public void storeModerateInfo(ModerateInfo mi) throws StorageException {
        moderateDAO.storeModerateInfo(mi);
        messageModaratesCache.remove(mi.getMessageId());
    }

    public boolean removeModerateInfo(int id) throws StorageException {
        if (moderateDAO.removeModerateInfo(id)) {
            moderateCache.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public ModerateInfo getModerateInfoById(int id) throws StorageException {
        ModerateInfo forum = moderateCache.get(id);
        if (forum == null) {
            forum = moderateDAO.getModerateInfoById(id);
            if (forum != null) {
                moderateCache.put(id, forum);
            }
        }
        return forum;
    }

    public int[] getModerateIdsByMessageId(int messageId) throws StorageException {
        int[] moderates = messageModaratesCache.get(messageId);
        if (moderates != null) {
            moderates = moderateDAO.getModerateIdsByMessageId(messageId);
            if (moderates != null) {
                messageModaratesCache.put(messageId, moderates);
            }
        }
        return moderates;
    }

    public int[] getModerateIdsByUserIds(int userId) throws StorageException {
        return moderateDAO.getModerateIdsByUserIds(userId);
    }

    public int[] getModerateIdsByForumIds(int forumId) throws StorageException {
        return moderateDAO.getModerateIdsByForumIds(forumId);
    }

    public int[] getAllModerateIds() throws StorageException {
        return moderateDAO.getAllModerateIds();
    }

    public void purge() {
        moderateCache.purge();
        messageModaratesCache.purge();
    }
}
