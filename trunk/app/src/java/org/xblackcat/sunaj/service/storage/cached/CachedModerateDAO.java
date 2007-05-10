package org.xblackcat.sunaj.service.storage.cached;

import org.xblackcat.sunaj.service.data.Moderate;
import org.xblackcat.sunaj.service.storage.IModerateDAO;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */
final class CachedModerateDAO implements IModerateDAO,IPurgable {
    private final Cache<Moderate[]> messageModaratesCache = new Cache<Moderate[]>();

    private final IModerateDAO moderateDAO;

    CachedModerateDAO(IModerateDAO moderateDAO) {
        this.moderateDAO = moderateDAO;
    }

    public void storeModerateInfo(Moderate mi) throws StorageException {
        moderateDAO.storeModerateInfo(mi);
        messageModaratesCache.remove(mi.getMessageId());
    }

    public boolean removeModerateInfosByMessageId(int id) throws StorageException {
        if (moderateDAO.removeModerateInfosByMessageId(id)) {
            messageModaratesCache.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public Moderate[] getModeratesByMessageId(int messageId) throws StorageException {
        Moderate[] moderates = messageModaratesCache.get(messageId);
        if (moderates != null) {
            moderates = moderateDAO.getModeratesByMessageId(messageId);
            if (moderates != null) {
                messageModaratesCache.put(messageId, moderates);
            }
        }
        return moderates;
    }

    public void purge() {
        messageModaratesCache.purge();
    }
}
