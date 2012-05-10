package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.data.ReadStatistic;
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
    public DiscussionStatistic getReplaysInThread(int threadId, int userId) throws StorageException {
        return helper.executeSingle(Converters.TO_DISCUSSION_STATISTIC, DataQuery.GET_MESSAGES_STATISTIC_IN_THREAD, userId, threadId);
    }

    @Override
    public ReadStatistic getTotals(int userId) throws StorageException {
        final int messages = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_MESSAGES_NUMBER).intValue();
        final int unreadMessages = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_MESSAGES_NUMBER).intValue();
        final int unreadReplies;
        if (unreadMessages > 0) {
            unreadReplies = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_USER_REPLIES_NUMBER, userId).intValue();
        } else {
            unreadReplies = 0;
        }

        return new ReadStatistic(unreadReplies, unreadMessages, messages);
    }

    @Override
    public ReadStatistic getUserRepliesStat(int userId) throws StorageException {
        // TODO: make single request instead of double
        int unread = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_USER_REPLIES_NUMBER, userId).intValue();
        int total = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_USER_REPLIES_NUMBER, userId).intValue();
        return new ReadStatistic(0, unread, total);
    }

    @Override
    public ReadStatistic getUserPostsStat(int userId) throws StorageException {
        // TODO: make single request instead of double
        int unread = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_UNREAD_USER_POSTS_NUMBER, userId).intValue();
        int total = helper.executeSingle(Converters.TO_NUMBER, DataQuery.GET_USER_POSTS_NUMBER, userId).intValue();
        return new ReadStatistic(0, unread, total);
    }
}
