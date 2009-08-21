package org.xblackcat.rojac.service.storage.cached;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */
final class CachedMessageAH implements IMessageAH, IPurgable {
    private final Cache<Message> messageCache = new Cache<Message>();
    private final Cache<int[]> childrenMessagesCache = new Cache<int[]>();

    private final IMessageAH messageAH;

    CachedMessageAH(IMessageAH messageAH) {
        this.messageAH = messageAH;
    }

    public void storeMessage(Message fm) throws StorageException {
        messageAH.storeMessage(fm);
        if (fm.getParentId() != 0) {
            childrenMessagesCache.remove(fm.getParentId());
        }
    }

    public boolean removeForumMessage(int id) throws StorageException {
        if (messageAH.removeForumMessage(id)) {
            messageCache.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public Message getMessageById(int messageId) throws StorageException {
        Message message = messageCache.get(messageId);
        if (message == null) {
            message = messageAH.getMessageById(messageId);
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
            messages = messageAH.getMessageIdsByParentId(parentMessageId);
            if (messages != null && parentMessageId != 0) {
                childrenMessagesCache.put(parentMessageId, messages);
            }
        }
        return messages;
    }

    public int[] getMessageIdsByTopicId(int topicId) throws StorageException {
        return messageAH.getMessageIdsByTopicId(topicId);
    }

    public int[] getTopicMessageIdsByForumId(int forumId) throws StorageException {
        return messageAH.getTopicMessageIdsByForumId(forumId);
    }

    public int[] getBrokenTopicIds() throws StorageException {
        return messageAH.getBrokenTopicIds();
    }

    public void updateMessage(Message mes) throws StorageException {
        messageCache.remove(mes.getMessageId());
        messageAH.updateMessage(mes);
    }

    public void updateMessageRecentDate(int messageId, long recentDate) throws StorageException {
        messageAH.updateMessageRecentDate(messageId, recentDate);
    }

    public void updateMessageReadFlag(int messageId, boolean read) throws StorageException {
        messageAH.updateMessageReadFlag(messageId, read);
    }

    public boolean isExist(int messageId) throws StorageException {
        return messageAH.isExist(messageId);
    }

    @Override
    public int getParentIdByMessageId(int messageId) throws StorageException {
        return messageAH.getParentIdByMessageId(messageId);
    }

    public int[] getAllMessageIds() throws StorageException {
        return messageAH.getAllMessageIds();
    }

    public void purge() {
        messageCache.purge();
        childrenMessagesCache.purge();
    }
}