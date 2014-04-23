package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.service.storage.database.convert.ToDiscussionStatisticConverter;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.Sql;
import org.xblackcat.sjpu.storage.ann.ToObjectConverter;

/**
 * @author xBlackCat
 */
public interface IStatisticAH extends IAH {

    /**
     * Returns number of replies in specified thread (total/non-read)
     *
     * @param threadId target thread id.
     * @param userId
     * @return brief stat data.
     */
    @Sql("SELECT count(m.id), (SELECT count(um.id) FROM message um WHERE um.topic_id=m.topic_id AND um.`read` = FALSE AND um.forum_id = m.forum_id AND NOT CASEWHEN(um.user_id = 0, FALSE, (SELECT iu.ignored FROM user iu WHERE iu.id = um.user_id))) AS unreadmessages, (SELECT count(m1.id) FROM message m1 WHERE NOT CASEWHEN(m1.user_id = 0, FALSE, (SELECT iu.ignored FROM user iu WHERE iu.id = m1.user_id)) AND tc.ignored = FALSE AND m1.read = FALSE AND m1.parent_user_id <> m1.user_id AND m1.topic_id = m.topic_id AND m1.parent_user_id = ? AND m1.forum_id = m.forum_id), max(m.message_date) FROM topic_cache tc JOIN message m ON (tc.topic_id = m.topic_id AND m.forum_id = tc.forum_id) WHERE m.topic_id=?")
    @ToObjectConverter(ToDiscussionStatisticConverter.class)
    DiscussionStatistic getReplaysInThread(int threadId, int userId) throws StorageException;

    /**
     * Returns number of replies to specified user.
     *
     * @param userId target user id.
     * @return brief stat data.
     */
//    ReadStatistic getUserRepliesStat(int userId) throws StorageException;

    /**
     * Returns number of posts of specified user.
     *
     * @param userId target user id.
     * @return brief stat data.
     */
//    ReadStatistic getUserPostsStat(int userId) throws StorageException;

    @Sql("SELECT count(m.id) FROM message m WHERE NOT CASEWHEN(m.user_id = 0, false, (SELECT iu.ignored FROM user iu WHERE iu.id = m.user_id)) AND (SELECT tc.ignored FROM topic_cache tc WHERE tc.topic_id = CASEWHEN(m.topic_id = 0, m.id, m.topic_id)) = false AND m.user_id <> m.parent_user_id AND m.parent_user_id = ? AND m.forum_id > 0")
    int getUserReplies(int ownId) throws StorageException;
}
