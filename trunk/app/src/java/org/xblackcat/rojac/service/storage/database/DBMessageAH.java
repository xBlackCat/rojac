package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Message;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.Role;
import org.xblackcat.rojac.data.ThreadStatData;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;
import ru.rsdn.Janus.JanusMessageInfo;

import java.util.Collection;

/**
 * @author ASUS
 */

final class DBMessageAH implements IMessageAH {
    private final IQueryExecutor helper;

    DBMessageAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeMessage(JanusMessageInfo fm) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_MESSAGE,
                fm.getMessageId(),
                fm.getTopicId(),
                fm.getParentId(),
                fm.getUserId(),
                fm.getForumId(),
                fm.getArticleId(),
                fm.getUserTitleColor(),
                Role.getUserType(fm.getUserRole()).ordinal(),
                fm.getMessageDate().getTimeInMillis(),
                fm.getUpdateDate().getTimeInMillis(),
                fm.getLastModerated().getTimeInMillis(),
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
        return helper.executeSingle(Converters.TO_MESSAGE, DataQuery.GET_OBJECT_MESSAGE, messageId);
    }

    public String getMessageBodyById(int messageId) throws StorageException {
        return helper.executeSingle(Converters.TO_STRING, DataQuery.GET_OBJECT_MESSAGE_BODY, messageId);
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

    public int[] getBrokenTopicIds() throws StorageException {
        return helper.getIds(DataQuery.GET_BROKEN_TOPIC_IDS);
    }

    public void updateMessage(JanusMessageInfo m) throws StorageException {
        helper.update(DataQuery.UPDATE_OBJECT_MESSAGE,
                m.getTopicId(),
                m.getParentId(),
                m.getUserId(),
                m.getForumId(),
                m.getArticleId(),
                m.getUserTitleColor(),
                Role.getUserType(m.getUserRole()).ordinal(),
                m.getMessageDate().getTimeInMillis(),
                m.getUpdateDate().getTimeInMillis(),
                m.getLastModerated().getTimeInMillis(),
                m.getSubject(),
                m.getMessageName(),
                m.getUserNick(),
                m.getUserTitle(),
                m.getMessage(),
                m.getMessageId());
    }

    public void updateMessageRecentDate(int messageId, long recentDate) throws StorageException {
        helper.update(DataQuery.UPDATE_MESSAGE_RECENT_CHILD_DATE,
                recentDate,
                messageId);
    }

    public void updateMessageReadFlag(int messageId, boolean read) throws StorageException {
        helper.update(DataQuery.UPDATE_MESSAGE_READ_FLAG,
                read,
                messageId);
    }

    public boolean isExist(int messageId) throws StorageException {
        return helper.executeSingle(Converters.TO_BOOLEAN,
                DataQuery.IS_MESSAGES_EXIST,
                messageId);
    }

    public int getParentIdByMessageId(int messageId) throws StorageException {
        return helper.executeSingle(Converters.TO_INTEGER, DataQuery.GET_PARENT_ID_FOR_MESSAGE_ID, messageId);
    }

    @Override
    public MessageData[] getMessageDatasByTopicId(int threadId) throws StorageException {
        Collection<MessageData> datas = helper.execute(
                Converters.TO_MESSAGE_DATA,
                DataQuery.GET_OBJECTS_MESSAGE_DATA,
                threadId);
        return datas.toArray(new MessageData[datas.size()]);
    }

    @Override
    public MessageData[] getTopicMessageDatasByForumId(int forumId) throws StorageException {
        Collection<MessageData> datas = helper.execute(
                Converters.TO_MESSAGE_DATA,
                DataQuery.GET_TOPIC_MESSAGE_DATA_BY_FORUM_ID,
                forumId);
        return datas.toArray(new MessageData[datas.size()]);
    }

    @Override
    public ThreadStatData getThreadStatByThreadId(int threadId) throws StorageException {
        return helper.executeSingle(Converters.TO_THREAD_DATA, DataQuery.GET_THREAD_STAT_DATA, threadId);
    }

    @Override
    public int getUnreadReplaysInThread(int threadId) throws StorageException {
        return helper.executeSingle(Converters.TO_INTEGER, DataQuery.GET_UNREAD_MESSAGES_NUMBER_IN_THREAD, threadId);
    }

    public int[] getAllMessageIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_MESSAGE);
    }
}
