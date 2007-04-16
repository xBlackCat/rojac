package org.xblackcat.sunaj.service.storage.cached;

import org.xblackcat.sunaj.service.data.NewMessage;
import org.xblackcat.sunaj.service.storage.INewMessageDAO;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 16.04.2007
*
* @author ASUS
*/
final class CachedNewMessageDAO implements INewMessageDAO,IPurgable {
    private final INewMessageDAO newMessageDAO;

    CachedNewMessageDAO(INewMessageDAO newMessageDAO) {
        this.newMessageDAO = newMessageDAO;
    }

    public void storeNewMessage(NewMessage nm) throws StorageException {
        newMessageDAO.storeNewMessage(nm);
    }

    public boolean removeNewMessage(int id) throws StorageException {
        return newMessageDAO.removeNewMessage(id);
    }

    public NewMessage getNewMessageById(int id) throws StorageException {
        return newMessageDAO.getNewMessageById(id);
    }

    public int[] getAllNewMessageIds() throws StorageException {
        return newMessageDAO.getAllNewMessageIds();
    }

    public void purge() {
    }
}
