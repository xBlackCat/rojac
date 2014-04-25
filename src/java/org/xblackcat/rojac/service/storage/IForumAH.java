package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.rojac.service.storage.database.convert.ToForumDataConverter;
import org.xblackcat.rojac.service.storage.database.convert.ToRequestForumInfoConverter;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.MapRowTo;
import org.xblackcat.sjpu.storage.ann.Sql;
import org.xblackcat.sjpu.storage.ann.ToObjectConverter;
import ru.rsdn.janus.RequestForumInfo;

import java.util.List;

/**
 * @author ASUS
 */

public interface IForumAH extends IAH {
    @Sql("INSERT INTO forum (id, forum_group_id, rated, in_top, rate_limit, subscribed, short_name, name)\n" +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
    void storeForum(
            int forumId,
            int forumGroupId,
            int rated,
            int inTop,
            int rateLimit,
            boolean subscribed,
            String shortForumName,
            String forumName
    ) throws StorageException;

    @Sql("SELECT\n" +
                 "  id, forum_group_id, rated, in_top, rate_limit, subscribed, short_name, name\n" +
                 "FROM forum\n" +
                 "WHERE id = ?")
    Forum getForumById(int forumId) throws StorageException;

    @Sql("SELECT\n" +
                 "  f.id, (\n" +
                 "              SELECT\n" +
                 "                m.id\n" +
                 "              FROM message m\n" +
                 "              WHERE m.forum_id = f.id\n" +
                 "              LIMIT 1) IS NULL\n" +
                 "FROM forum f\n" +
                 "WHERE f.subscribed = TRUE")
    @ToObjectConverter(ToRequestForumInfoConverter.class)
    List<RequestForumInfo> getSubscribedForums() throws StorageException;

    /**
     * Updates forum information. Notice that {@code isSubscribed}  field is not changed during operation.
     *
     * @param forumGroupId
     * @param rated
     * @param inTop
     * @param rateLimit
     * @param shortForumName
     * @param forumName
     * @param forumId
     * @throws StorageException
     */
    @Sql("UPDATE forum\n" +
                 "SET forum_group_id = ?, rated = ?, in_top = ?, rate_limit = ?, short_name = ?, name = ?\n" +
                 "WHERE id = ?")
    void updateForum(
            int forumGroupId,
            int rated,
            int inTop,
            int rateLimit,
            String shortForumName,
            String forumName,
            int forumId
    ) throws StorageException;

    @Sql("UPDATE forum\n" +
                 "SET subscribed = ?\n" +
                 "WHERE id = ?")
    void setSubscribeForum(int forumId, boolean subscribe) throws StorageException;

    @Sql("UPDATE message\n" +
                 "SET read = ?\n" +
                 "WHERE forum_id = ?")
    void setForumRead(int forumId, boolean read) throws StorageException;

    /**
     * Returns list of all available forums.
     *
     * @return list of all available forums.
     * @throws StorageException
     */
    @Sql("SELECT\n" +
                 "  id, forum_group_id, rated, in_top, rate_limit, subscribed, short_name, name, f.totalposts, 0, 0, f.lastpost_date\n" +
                 "FROM forum f")
    @ToObjectConverter(ToForumDataConverter.class)
    List<ItemStatisticData<Forum>> getAllForums() throws StorageException;

    @Sql("SELECT\n" +
                 "  f.totalposts,\n" +
                 "  (\n" +
                 "    SELECT\n" +
                 "  count(um.id)\n" +
                 "    FROM message um\n" +
                 "    WHERE um.`read` = FALSE AND um.forum_id = f.id AND NOT CASEWHEN(um.user_id = 0, FALSE, (\n" +
                 "      SELECT\n" +
                 "        iu.ignored\n" +
                 "      FROM user iu\n" +
                 "      WHERE iu.id = um.user_id)) AND (\n" +
                 "                                               SELECT\n" +
                 "                                                 tc.ignored\n" +
                 "                                               FROM topic_cache tc\n" +
                 "                                               WHERE tc.topic_id = CASEWHEN(um.topic_id = 0, um.id, um.topic_id)) =\n" +
                 "                                             FALSE)                   AS unreadmessages,\n" +
                 "  (\n" +
                 "    SELECT\n" +
                 "  count(m.id)\n" +
                 "    FROM message m\n" +
                 "    WHERE NOT CASEWHEN(m.user_id = 0, FALSE, (\n" +
                 "      SELECT\n" +
                 "        iu.ignored\n" +
                 "      FROM user iu\n" +
                 "      WHERE iu.id = m.user_id)) AND (\n" +
                 "                                              SELECT\n" +
                 "                                                tc.ignored\n" +
                 "                                              FROM\n" +
                 "                                                topic_cache tc\n" +
                 "                                              WHERE tc.topic_id = CASEWHEN(\n" +
                 "                                                  m.topic_id = 0,\n" +
                 "                                                  m.id,\n" +
                 "                                                  m.topic_id)) = FALSE AND m.`read` = FALSE AND m.parent_user_id <> m.user_id\n" +
                 "          AND m.forum_id = f.id AND m.parent_user_id = ?) AS unread,\n" +
                 "  f.lastpost_date\n" +
                 "FROM forum f\n" +
                 "WHERE f.id = ?;")
    DiscussionStatistic getForumStatistic(int forumId, int userId) throws StorageException;

    @Sql("SELECT\n" +
                 "  count(f.id) > 0\n" +
                 "FROM forum f\n" +
                 "WHERE f.subscribed = TRUE")
    boolean hasSubscribedForums() throws StorageException;

    @Sql("SELECT\n" +
                 "  id, forum_group_id, rated, in_top, rate_limit, subscribed, short_name, name\n" +
                 "FROM forum")
    @MapRowTo(Forum.class)
    List<Forum> getForumLists() throws StorageException;

    @Sql("UPDATE forum f\n" +
                 "SET f.totalposts                                      = ((\n" +
                 "  SELECT\n" +
                 "    count(mm.id)\n" +
                 "  FROM message mm\n" +
                 "  WHERE mm.forum_id = f.id)), f.lastpost_date = ((\n" +
                 "  SELECT\n" +
                 "    max(mm.message_date)\n" +
                 "  FROM message mm\n" +
                 "  WHERE mm.forum_id = f.id))\n" +
                 "WHERE f.id = ?")
    void updateForumStatistic(int forumId) throws StorageException;
}
