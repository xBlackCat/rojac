package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.NewMessage;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface INewMessageDAO {
    void storeNewMessage(NewMessage nm) throws StorageException;

    boolean removeNewMessage(int id) throws StorageException;

    NewMessage getNewMessageById(int id) throws StorageException;

    int[] getAllNewMessageIds() throws StorageException;
}
