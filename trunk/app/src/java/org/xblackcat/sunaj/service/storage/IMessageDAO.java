package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.Message;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IMessageDAO {
    void storeForumMessage(Message fm) throws StorageException;

    boolean removeForumMessage(int id) throws StorageException;

    Message getMessageById(int messageId) throws StorageException;

    int[] getMessageIdsByParentId(int parentMessageId) throws StorageException;

    int[] getMessageIdsByTopicId(int topicId) throws StorageException;

    int[] getAllMessageIds() throws StorageException;

    int[] getTopicMessageIdsByForumId(int forumId) throws StorageException;
}
