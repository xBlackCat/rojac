package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.ThreadStatData;
import ru.rsdn.Janus.JanusMessageInfo;

/**
 * @author ASUS
 */

public interface IMessageAH extends AH {
    /**
     * Stores a message record by id.
     *
     * @param fm   message data to be stored into database.
     * @param read read state of the message. <code>true</code> - message is read and <code>false</code> otherwise.
     *
     * @throws StorageException
     */
    void storeMessage(JanusMessageInfo fm, boolean read) throws StorageException;

    boolean removeForumMessage(int id) throws StorageException;

    String getMessageBodyById(int messageId) throws StorageException;

    int[] getBrokenTopicIds() throws StorageException;

    /**
     * Updates a message record by id.
     *
     * @param mes  message data to be stored into database.
     * @param read read state of the message. <code>true</code> - message is read and <code>false</code> otherwise.
     *
     * @throws StorageException
     */
    void updateMessage(JanusMessageInfo mes, boolean read) throws StorageException;

    void updateMessageReadFlag(int messageId, boolean read) throws StorageException;

    boolean isExist(int messageId) throws StorageException;

    /**
     * Loads messages of the specified thread.
     *
     * @param threadId
     *
     * @return array of messages data.
     *
     * @throws StorageException
     */
    MessageData[] getMessagesDataByTopicId(int threadId) throws StorageException;

    /**
     * Returns messages data for specified forum id.
     *
     * @param forumId target forum id
     *
     * @return array of topic messages data.
     *
     * @throws StorageException
     */
    MessageData[] getTopicMessagesDataByForumId(int forumId) throws StorageException;

    ThreadStatData getThreadStatByThreadId(int forumId) throws StorageException;

    int getUnreadReplaysInThread(int threadId) throws StorageException;

    MessageData getMessageData(int messageId) throws StorageException;

    int getUnreadMessages() throws StorageException;

    int getUnreadReplies(int userId) throws StorageException;
}
