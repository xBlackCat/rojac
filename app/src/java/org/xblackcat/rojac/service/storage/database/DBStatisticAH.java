package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.ReadStatistic;
import org.xblackcat.rojac.data.ThreadStatData;
import org.xblackcat.rojac.data.UnreadStatData;
import org.xblackcat.rojac.service.storage.IStatisticAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
 * @author xBlackCat
 */
final class DBStatisticAH extends AnAH implements IStatisticAH {
    DBStatisticAH(IQueryHolder helper) {
        super(helper);
    }

    @Override
    public ThreadStatData getThreadStatByThreadId(int threadId) throws StorageException {
        return helper.executeSingle(Converters.TO_THREAD_DATA, DataQuery.GET_THREAD_STAT_DATA, threadId);
    }

    @Override
    public UnreadStatData getReplaysInThread(int threadId) throws StorageException {
        int unread = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_MESSAGES_NUMBER_IN_THREAD, threadId).intValue();
        int total = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_MESSAGES_NUMBER_IN_THREAD, threadId).intValue();
        return new UnreadStatData(unread, total);
    }

    @Override
    public ReadStatistic getTotals(int userId) throws StorageException {
        final int messages = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_MESSAGES_NUMBER).intValue();
        final int unreadMessages = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_MESSAGES_NUMBER).intValue();
        final int unreadReplies = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_USER_REPLIES_NUMBER, userId).intValue();

        return new ReadStatistic(unreadReplies, unreadMessages, messages);
    }

    @Override
    public UnreadStatData getUserRepliesStat(int userId) throws StorageException {
        int unread = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_USER_REPLIES_NUMBER, userId).intValue();
        int total = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_USER_REPLIES_NUMBER, userId).intValue();
        return new UnreadStatData(unread, total);
    }

    @Override
    public UnreadStatData getUserPostsStat(int userId) throws StorageException {
        int unread = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_USER_POSTS_NUMBER, userId).intValue();
        int total = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_USER_POSTS_NUMBER, userId).intValue();
        return new UnreadStatData(unread, total);
    }
}
