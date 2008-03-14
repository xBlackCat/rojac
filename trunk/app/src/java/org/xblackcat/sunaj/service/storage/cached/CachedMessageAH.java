package org.xblackcat.sunaj.service.storage.cached;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.sunaj.service.data.Message;
import org.xblackcat.sunaj.service.storage.IMessageAH;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */
final class CachedMessageAH implements IMessageAH, IPurgable {
    private final Cache<Message> messageCache = new Cache<Message>();
    private final Cache<int[]> childrenMessagesCache = new Cache<int[]>();

    private final IMessageAH messageDAO;

    CachedMessageAH(IMessageAH messageDAO) {
        this.messageDAO = messageDAO;
    }

    public void storeMessage(Message fm) throws StorageException {
        messageDAO.storeMessage(fm);
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

    public String getMessageBodyById(int messageId) throws StorageException {
        throw new NotImplementedException();
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

    public void updateMessage(Message mes) throws StorageException {
        messageCache.remove(mes.getMessageId());
        messageDAO.updateMessage(mes);
    }

    public int[] getAllMessageIds() throws StorageException {
        return messageDAO.getAllMessageIds();
    }

    public void purge() {
        messageCache.purge();
        childrenMessagesCache.purge();
    }
}
