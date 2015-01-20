package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.NewMessage;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.MapRowTo;
import org.xblackcat.sjpu.storage.ann.Sql;

import java.util.List;

/**
 * @author ASUS
 */

public interface INewMessageAH extends IAH {
    @Sql("INSERT INTO new_message( parent_id, forum_id, subject, message, draft) VALUES (?, ?, ?, ?, ?)")
    void storeNewMessage(int parentId, int forumId, String subject, String message, boolean draft) throws StorageException;

    @Sql("UPDATE new_message SET subject=?, message=?, draft = ? WHERE id=?")
    void updateNewMessage(String subject, String message, boolean draft, int localMessageId) throws StorageException;

    @Sql("DELETE FROM new_message WHERE id=?")
    int removeNewMessage(int id) throws StorageException;

    @Sql("DELETE FROM new_message")
    void purgeNewMessage() throws StorageException;

    @Sql("SELECT id, parent_id, forum_id, subject, message, draft FROM new_message WHERE id=?")
    NewMessage getNewMessageById(int id) throws StorageException;

    @Sql("SELECT id, parent_id, forum_id, subject, message, draft FROM new_message")
    @MapRowTo(NewMessage.class)
    List<NewMessage> getAllNewMessages() throws StorageException;

    @Sql("SELECT id, parent_id, forum_id, subject, message, draft FROM new_message WHERE draft = 0")
    @MapRowTo(NewMessage.class)
    List<NewMessage> getNewMessagesToSend() throws StorageException;

    @Sql("UPDATE new_message SET draft=? WHERE id=?")
    void setDraftFlag(boolean draft, int messageId) throws StorageException;
}
