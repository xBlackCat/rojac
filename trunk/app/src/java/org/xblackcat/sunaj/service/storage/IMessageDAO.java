package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.ForumMessage;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IMessageDAO {
    void storeForumMessage(ForumMessage fm) throws StorageException;

    boolean removeForumMessage(int id) throws StorageException;

    ForumMessage getMessageById(int messageId) throws StorageException;

    int[] getMessageIdsByParentId(int parentMessageId) throws StorageException;

    int[] getMessageIdsByTopicId(int topicId) throws StorageException;

    int[] getMessageIdsByUserId(int userId) throws StorageException;

    int[] getMessageIdsByForumId(int forumId) throws StorageException;

    int[] getMessageIdsByParentAndTopicIds(int parentId, int topicId) throws StorageException;

    int[] getAllMessageIds() throws StorageException;
}
