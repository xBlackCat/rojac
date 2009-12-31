package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Message;
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
     * @param fm  message data to be stored into database.
     * @param read read state of the message. <code>true</code> - message is read and <code>false</code> otherwise.
     *
     * @throws StorageException
     */
    void storeMessage(JanusMessageInfo fm, boolean read) throws StorageException;

    boolean removeForumMessage(int id) throws StorageException;

    Message getMessageById(int messageId) throws StorageException;

    String getMessageBodyById(int messageId) throws StorageException;

    int[] getMessageIdsByParentId(int parentMessageId) throws StorageException;

    int[] getMessageIdsByTopicId(int topicId) throws StorageException;

    int[] getAllMessageIds() throws StorageException;

    int[] getTopicMessageIdsByForumId(int forumId) throws StorageException;

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

    void updateMessageRecentDate(int messageId, long recentDate) throws StorageException;

    void updateMessageReadFlag(int messageId, boolean read) throws StorageException;

    boolean isExist(int messageId) throws StorageException;

    int getParentIdByMessageId(int messageId) throws StorageException;

    /**
     * Loads messages of the specified thread.
     *
     * @param threadId
     *
     * @return array of messages data.
     */
    MessageData[] getMessageDatasByTopicId(int threadId) throws StorageException;

    MessageData[] getTopicMessageDatasByForumId(int threadId) throws StorageException;

    ThreadStatData getThreadStatByThreadId(int forumId) throws StorageException;

    int getUnreadReplaysInThread(int threadId) throws StorageException;

    MessageData getMessageData(int messageId) throws StorageException;
}
