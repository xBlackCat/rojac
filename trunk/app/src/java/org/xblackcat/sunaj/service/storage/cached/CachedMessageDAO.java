package org.xblackcat.sunaj.service.storage.cached;

import org.xblackcat.sunaj.service.data.Message;
import org.xblackcat.sunaj.service.storage.IMessageDAO;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */
final class CachedMessageDAO implements IMessageDAO, IPurgable {
    private final Cache<Message> messageCache = new Cache<Message>();
    private final Cache<int[]> childrenMessagesCache = new Cache<int[]>();

    private final IMessageDAO messageDAO;

    CachedMessageDAO(IMessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public void storeForumMessage(Message fm) throws StorageException {
        messageDAO.storeForumMessage(fm);
        if (fm.getParentId() != 0) {
            childrenMessagesCache.remove(fm.getParentId());
        }
    }

    public boolean removeForumMessage(int id) throws StorageException {
        if (messageDAO.removeForumMessage(id)) {
            messageCache.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public Message getMessageById(int messageId) throws StorageException {
        Message message = messageCache.get(messageId);
        if (message == null) {
            message = messageDAO.getMessageById(messageId);
            if (message != null) {
                messageCache.put(messageId, message);
            }
        }
        return message;
    }

    public int[] getMessageIdsByParentId(int parentMessageId) throws StorageException {
        int[] messages = childrenMessagesCache.get(parentMessageId);
        if (messages == null) {
            messages = messageDAO.getMessageIdsByParentId(parentMessageId);
            if (messages != null && parentMessageId != 0) {
                childrenMessagesCache.put(parentMessageId, messages);
            }
        }
        return messages;
    }

    public int[] getMessageIdsByTopicId(int topicId) throws StorageException {
        return messageDAO.getMessageIdsByTopicId(topicId);
    }

    public int[] getTopicMessageIdsByForumId(int forumId) throws StorageException {
        return messageDAO.getTopicMessageIdsByForumId(forumId);
    }

    public boolean isMessagesExistInForum(int forumId) throws StorageException {
        return messageDAO.isMessagesExistInForum(forumId);
    }

    public int[] getAllMessageIds() throws StorageException {
        return messageDAO.getAllMessageIds();
    }

    public void purge() {
        messageCache.purge();
        childrenMessagesCache.purge();
    }
}
