package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.data.NewMessage;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface INewMessageAH extends AH {
    void storeNewMessage(NewMessage nm) throws StorageException;

    boolean removeNewMessage(int id) throws StorageException;

    NewMessage getNewMessageById(int id) throws StorageException;

    NewMessage[] getAllNewMessages() throws StorageException;
}
