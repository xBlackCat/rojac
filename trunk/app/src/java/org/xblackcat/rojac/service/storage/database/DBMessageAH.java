package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.*;
import org.xblackcat.rojac.service.datahandler.SynchronizationCompletePacket;
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

    public void storeMessage(JanusMessageInfo fm, boolean read) throws StorageException {
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
                fm.getMessage(),
                read);
    }

    public boolean removeForumMessage(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_MESSAGE, id) > 0;
    }

    public String getMessageBodyById(int messageId) throws StorageException {
        return helper.executeSingle(Converters.TO_STRING, DataQuery.GET_OBJECT_MESSAGE_BODY, messageId);
    }

    public int[] getBrokenTopicIds() throws StorageException {
        return helper.getIds(DataQuery.GET_BROKEN_TOPIC_IDS);
    }

    public void updateMessage(JanusMessageInfo m, boolean read) throws StorageException {
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
                read,
                m.getMessageId());
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

    @Override
    public Collection<MessageData> getMessagesDataByTopicId(int threadId, int forumId) throws StorageException {
        return helper.execute(
                Converters.TO_MESSAGE_DATA,
                DataQuery.GET_OBJECTS_MESSAGE_DATA,
                threadId,
                forumId);
    }

    @Override
    public Iterable<MessageData> getTopicMessagesDataByForumId(int forumId) throws StorageException {
        return helper.execute(
                Converters.TO_MESSAGE_DATA,
                DataQuery.GET_TOPIC_MESSAGE_DATA_BY_FORUM_ID,
                forumId);
    }

    @Override
    public Iterable<MessageData> getUserPosts(int userId) throws StorageException {
        return helper.execute(
                Converters.TO_MESSAGE_DATA,
                DataQuery.GET_OBJECTS_MESSAGE_DATA_USER_POSTS,
                userId);
    }

    @Override
    public Collection<MessageData> getUserReplies(int userId) throws StorageException {
        return helper.execute(
                Converters.TO_MESSAGE_DATA,
                DataQuery.GET_OBJECTS_MESSAGE_DATA_USER_REPLIES,
                userId);
    }

    @Override
    public ThreadStatData getThreadStatByThreadId(int threadId) throws StorageException {
        return helper.executeSingle(Converters.TO_THREAD_DATA, DataQuery.GET_THREAD_STAT_DATA, threadId);
    }

    @Override
    public FavoriteStatData getReplaysInThread(int threadId) throws StorageException {
        int unread = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_MESSAGES_NUMBER_IN_THREAD, threadId).intValue();
        int total = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_MESSAGES_NUMBER_IN_THREAD, threadId).intValue();
        return new FavoriteStatData(unread, total);
    }

    @Override
    public MessageData getMessageData(int messageId) throws StorageException {
        return helper.executeSingle(Converters.TO_MESSAGE_DATA, DataQuery.GET_OBJECT_MESSAGE_DATA, messageId);
    }

    @Override
    public int getUnreadMessages() throws StorageException {
        return helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_MESSAGES_NUMBER).intValue();
    }

    @Override
    public FavoriteStatData getUserRepliesStat(int userId) throws StorageException {
        int unread = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_USER_REPLIES_NUMBER, userId).intValue();
        int total = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_USER_REPLIES_NUMBER, userId).intValue();
        return new FavoriteStatData(unread, total);
    }

    @Override
    public FavoriteStatData getUserPostsStat(int userId) throws StorageException {
        int unread = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_USER_POSTS_NUMBER, userId).intValue();
        int total = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_USER_POSTS_NUMBER, userId).intValue();
        return new FavoriteStatData(unread, total);
    }

    @Override
    public void updateThreadReadFlag(int topicId, boolean read) throws StorageException {
        helper.update(DataQuery.UPDATE_MESSAGE_READ_FLAG,
                read,
                topicId);
        helper.update(DataQuery.UPDATE_THREAD_READ_FLAG,
                read,
                topicId);
    }

    @Override
    public void updateMessageRatingCache(int id, String ratingsCache) throws StorageException {
        helper.update(DataQuery.UPDATE_MESSAGE_RATING_CACHE,
                ratingsCache,
                id);
    }


    public SynchronizationCompletePacket setThreadReadBeforeDate(long dateline, boolean read, int forumId, int threadId) throws StorageException {
        Iterable<AffectedMessage> toUpdate = helper.execute(
                Converters.TO_AFFECTED_MESSAGE_CONVERTER,
                DataQuery.GET_TOPIC_MESSAGES_READ_FLAG_BEFORE,
                read,
                dateline,
                forumId,
                threadId,
                threadId
        );
        SynchronizationCompletePacket result = new SynchronizationCompletePacket(toUpdate);

        helper.update(DataQuery.UPDATE_TOPIC_MESSAGES_READ_FLAG_BEFORE, read, dateline, forumId, threadId, threadId);

        return result;
    }

    @Override
    public SynchronizationCompletePacket setThreadReadAfterDate(long dateline, boolean read, int forumId, int threadId) throws StorageException {
        Iterable<AffectedMessage> toUpdate = helper.execute(
                Converters.TO_AFFECTED_MESSAGE_CONVERTER,
                DataQuery.GET_TOPIC_MESSAGES_READ_FLAG_AFTER,
                read,
                dateline,
                forumId,
                threadId,
                threadId
        );
        SynchronizationCompletePacket result = new SynchronizationCompletePacket(toUpdate);

        helper.update(DataQuery.UPDATE_TOPIC_MESSAGES_READ_FLAG_AFTER, read, dateline, forumId, threadId, threadId);

        return result;
    }

    @Override
    public SynchronizationCompletePacket setForumReadBeforeDate(long dateline, boolean read, int forumId) throws StorageException {
        Iterable<AffectedMessage> toUpdate = helper.execute(
                Converters.TO_AFFECTED_MESSAGE_CONVERTER,
                DataQuery.GET_FORUM_MESSAGES_READ_FLAG_BEFORE,
                read,
                dateline,
                forumId
        );
        SynchronizationCompletePacket result = new SynchronizationCompletePacket(toUpdate);

        helper.update(DataQuery.UPDATE_FORUM_MESSAGES_READ_FLAG_BEFORE, read, dateline, forumId);

        return result;
    }

    @Override
    public SynchronizationCompletePacket setForumReadAfterDate(long dateline, boolean read, int forumId) throws StorageException {
        Iterable<AffectedMessage> toUpdate = helper.execute(
                Converters.TO_AFFECTED_MESSAGE_CONVERTER,
                DataQuery.GET_FORUM_MESSAGES_READ_FLAG_AFTER,
                read,
                dateline,
                forumId
        );
        SynchronizationCompletePacket result = new SynchronizationCompletePacket(toUpdate);

        helper.update(DataQuery.UPDATE_FORUM_MESSAGES_READ_FLAG_AFTER, read, dateline, forumId);

        return result;
    }

    @Override
    public SynchronizationCompletePacket setReadBeforeDate(long dateline, boolean read) throws StorageException {
        Iterable<AffectedMessage> toUpdate = helper.execute(
                Converters.TO_AFFECTED_MESSAGE_CONVERTER,
                DataQuery.GET_MESSAGES_READ_FLAG_BEFORE,
                read,
                dateline
        );
        SynchronizationCompletePacket result = new SynchronizationCompletePacket(toUpdate);

        helper.update(DataQuery.UPDATE_MESSAGES_READ_FLAG_BEFORE, read, dateline);

        return result;
    }

    @Override
    public SynchronizationCompletePacket setReadAfterDate(long dateline, boolean read) throws StorageException {
        Iterable<AffectedMessage> toUpdate = helper.execute(
                Converters.TO_AFFECTED_MESSAGE_CONVERTER,
                DataQuery.GET_MESSAGES_READ_FLAG_AFTER,
                read,
                dateline
        );
        SynchronizationCompletePacket result = new SynchronizationCompletePacket(toUpdate);

        helper.update(DataQuery.UPDATE_MESSAGES_READ_FLAG_AFTER, read, dateline);

        return result;
    }
}
