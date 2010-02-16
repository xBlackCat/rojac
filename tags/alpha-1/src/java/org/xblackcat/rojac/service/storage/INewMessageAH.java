package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.NewMessage;

/**
 * @author ASUS
 */

public interface INewMessageAH extends AH {
    void storeNewMessage(NewMessage nm) throws StorageException;

    void updateNewMessage(NewMessage nm) throws StorageException;

    boolean removeNewMessage(int id) throws StorageException;

    NewMessage getNewMessageById(int id) throws StorageException;

    NewMessage[] getAllNewMessages() throws StorageException;
}
