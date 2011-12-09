package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.ReadStatistic;
import org.xblackcat.rojac.data.ThreadStatData;

/**
 * @author xBlackCat
 */
public interface IStatisticAH extends AH {
    /**
     * Loads a brief thread statistic (amount of replies, non-read messages, etc.)
     *
     * @param threadId target threads id
     * @return brief thread statistic.
     * @throws org.xblackcat.rojac.service.storage.StorageException
     * @see org.xblackcat.rojac.data.ThreadStatData
     */
    ThreadStatData getThreadStatByThreadId(int threadId) throws StorageException;

    /**
     * Returns number of replies in specified thread (total/non-read)
     *
     *
     *
     * @param threadId target thread id.
     * @param userId
     * @return brief stat data.
     * @throws org.xblackcat.rojac.service.storage.StorageException
     */
    ReadStatistic getReplaysInThread(int threadId, int userId) throws StorageException;

    ReadStatistic getTotals(int userId) throws StorageException;

    /**
     * Returns number of replies to specified user.
     *
     *
     * @param userId target user id.
     * @return brief stat data.
     * @throws org.xblackcat.rojac.service.storage.StorageException
     */
    ReadStatistic getUserRepliesStat(int userId) throws StorageException;

    /**
     * Returns number of posts of specified user.
     *
     *
     * @param userId target user id.
     * @return brief stat data.
     * @throws org.xblackcat.rojac.service.storage.StorageException
     */
    ReadStatistic getUserPostsStat(int userId) throws StorageException;
}