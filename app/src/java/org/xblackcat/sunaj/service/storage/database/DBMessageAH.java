package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.data.Message;
import org.xblackcat.sunaj.service.storage.IMessageAH;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.Converters;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

final class DBMessageAH implements IMessageAH {
    private final IQueryExecutor helper;

    DBMessageAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeMessage(Message fm) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_MESSAGE,
                fm.getMessageId(),
                fm.getTopicId(),
                fm.getParentId(),
                fm.getUserId(),
                fm.getForumId(),
                fm.getArticleId(),
                fm.getUserTitleColor(),
                fm.getUserRole().ordinal(),
                fm.isNotifyOnResponse(),
                fm.isRead(),
                fm.getFavoriteIndex(),
                fm.getMessageDate(),
                fm.getUpdateDate(),
                fm.getLastModerated(),
                fm.getSubject(),
                fm.getMessageName(),
                fm.getUserNick(),
                fm.getUserTitle(),
                fm.getMessage());
    }

    public boolean removeForumMessage(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_MESSAGE, id) > 0;
    }

    public Message getMessageById(int messageId) throws StorageException {
        return helper.executeSingle(Converters.TO_MESSAGE_CONVERTER, DataQuery.GET_OBJECT_MESSAGE, messageId);
    }

    public String getMessageBodyById(int messageId) throws StorageException {
        return helper.executeSingle(Converters.TO_STRING_CONVERTER, DataQuery.GET_OBJECT_MESSAGE_BODY, messageId);
    }

    public int[] getMessageIdsByParentId(int parentMessageId) throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_MESSAGE_BY_PARENT_ID, parentMessageId);
    }

    public int[] getMessageIdsByTopicId(int topicId) throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_MESSAGE_BY_TOPIC_ID, topicId);
    }

    public int[] getTopicMessageIdsByForumId(int forumId) throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_TOPIC_MESSAGE_BY_FORUM_ID, forumId);
    }

    public void updateMessage(Message m) throws StorageException {
        helper.update(DataQuery.UPDATE_OBJECT_MESSAGE,
                m.getTopicId(),
                m.getParentId(),
                m.getUserId(),
                m.getForumId(),
                m.getArticleId(),
                m.getUserTitleColor(),
                m.getUserRole().ordinal(),
                m.isNotifyOnResponse(),
                m.isRead(),
                m.getFavoriteIndex(),
                m.getMessageDate(),
                m.getUpdateDate(),
                m.getLastModerated(),
                m.getSubject(),
                m.getMessageName(),
                m.getUserNick(),
                m.getUserTitle(),
                m.getMessage(),
                m.getMessageId());
    }

    public boolean isExist(int messageId) throws StorageException {
        return helper.executeSingle(Converters.TO_BOOLEAN_CONVERTER,
                DataQuery.IS_MESSAGES_EXIST,
                messageId);
    }

    public int[] getAllMessageIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_MESSAGE);
    }
}
