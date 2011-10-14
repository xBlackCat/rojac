package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.NewMessage;

import java.util.Collection;

/**
 * @author ASUS
 */

public interface INewMessageAH extends AH {
    void storeNewMessage(NewMessage nm) throws StorageException;

    void updateNewMessage(NewMessage nm) throws StorageException;

    boolean removeNewMessage(int id) throws StorageException;

    void purgeNewMessage() throws StorageException;

    NewMessage getNewMessageById(int id) throws StorageException;

    Collection<NewMessage> getAllNewMessages() throws StorageException;

    Collection<NewMessage> getNewMessagesToSend() throws StorageException;

    void setDraftFlag(boolean draft, int messageId) throws StorageException;
}
