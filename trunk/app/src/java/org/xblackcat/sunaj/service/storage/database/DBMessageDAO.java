package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.service.data.Message;
import org.xblackcat.sunaj.service.storage.IMessageDAO;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.ToMessageConvertor;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

class DBMessageDAO implements IMessageDAO {
    private final IQueryExecutor helper;

    public DBMessageDAO(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeForumMessage(Message fm) throws StorageException {
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
        return helper.executeSingle(new ToMessageConvertor(), DataQuery.GET_OBJECT_MESSAGE, messageId);
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

    public int[] getAllMessageIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_MESSAGE);
    }
}
