package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.Role;
import org.xblackcat.rojac.service.storage.database.convert.ToThreadDataConverter;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.MapRowTo;
import org.xblackcat.sjpu.storage.ann.Sql;
import org.xblackcat.sjpu.storage.ann.ToObjectConverter;

import java.util.List;

/**
 * @author ASUS
 */

public interface IMessageAH extends IAH {
    /**
     * Stores a message record by id.
     *
     * @param messageId
     * @param topicId
     * @param parentId
     * @param userId
     * @param forumId
     * @param articleId
     * @param userTitleColor
     * @param userType
     * @param messageDate
     * @param updateDate
     * @param lastModeratedDate
     * @param subject
     * @param messageName
     * @param userNick
     * @param userTitle
     * @param message
     * @param read              read state of the message. <code>true</code> - message is read and <code>false</code> otherwise.
     * @throws StorageException
     */
    @Sql("INSERT INTO message (id, topic_id, parent_id, user_id, forum_id, article_id, user_title_color, user_role, message_date, update_date, moderated_date, subject, message_name, user_nick, user_title, message, read, rating)\n" +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '')")
    void storeMessage(
            int messageId,
            int topicId,
            int parentId,
            int userId,
            int forumId,
            int articleId,
            int userTitleColor,
            Role userType,
            long messageDate,
            long updateDate,
            long lastModeratedDate,
            String subject,
            String messageName,
            String userNick,
            String userTitle,
            String message,
            boolean read
    ) throws StorageException;

    /**
     * Loads a message body for specified message id.
     *
     * @param messageId id of message body to be loaded.
     * @return the message body.
     * @throws StorageException
     */
    @Sql("SELECT\n" +
                 "  message\n" +
                 "FROM message\n" +
                 "WHERE id = ?")
    String getMessageBodyById(int messageId) throws StorageException;

    /**
     * Returns a list of topics which roots have not loaded yet.
     *
     * @return topic ids to have been loaded.
     * @throws StorageException
     */
    @Sql("SELECT DISTINCT topic_id FROM message WHERE topic_id > 0 AND topic_id NOT IN (SELECT id FROM message WHERE topic_id = 0)")
    int[] getBrokenTopicIds() throws StorageException;

    /**
     * Updates a message record by id.
     *
     * @param topicId
     * @param parentId
     * @param userId
     * @param forumId
     * @param articleId
     * @param userTitleColor
     * @param userType
     * @param messageDate
     * @param updateDate
     * @param lastModeratedDate
     * @param subject
     * @param messageName
     * @param userNick
     * @param userTitle
     * @param message
     * @param read              read state of the message. <code>true</code> - message is read and <code>false</code> otherwise.
     * @param messageId
     * @throws StorageException
     */
    @Sql("UPDATE message SET topic_id=?, parent_id=?, user_id=?, forum_id=?, article_id=?, user_title_color=?, user_role=?, message_date=?, update_date=?, moderated_date=?, subject=?, message_name=?, user_nick=?, user_title=?, message=?, read=? WHERE id=?")
    void updateMessage(
            int topicId,
            int parentId,
            int userId,
            int forumId,
            int articleId,
            int userTitleColor,
            Role userType,
            long messageDate,
            long updateDate,
            long lastModeratedDate,
            String subject,
            String messageName,
            String userNick,
            String userTitle,
            String message,
            boolean read,
            int messageId
    ) throws StorageException;


    /**
     * Updates read flag of the specified message.
     *
     * @param messageId message id to be updated
     * @param read      new state of flag.
     * @throws StorageException
     */
    @Sql("UPDATE message SET read=? WHERE id=?")
    void updateMessageReadFlag(int messageId, boolean read) throws StorageException;

    /**
     * Checks if a message with specified id is exists.
     *
     * @param messageId message id to check
     * @return <code>true</code> if message already loaded and <code>false</code> elsewise.
     * @throws StorageException
     */
    @Sql("SELECT COUNT(id)>0 FROM message WHERE id=?")
    boolean isExist(int messageId) throws StorageException;

    /**
     * Loads messages of the specified thread.
     *
     * @param threadId
     * @param forumId
     * @return array of messages data.
     * @throws StorageException
     */
    @Sql("SELECT id, topic_id, parent_id, forum_id, user_id, subject, user_nick, message_date, update_date, read, rating, FALSE AS ignored, parent_user_id, CASEWHEN(m.user_id = 0, FALSE, (SELECT iu.ignored FROM user iu WHERE iu.id = m.user_id)) AS ignoreduser FROM message m WHERE topic_id = ? AND forum_id = ?")
    @MapRowTo(MessageData.class)
    List<MessageData> getMessagesDataByTopicId(int threadId, int forumId) throws StorageException;

    /**
     * Loads latest N topics.
     *
     * @param limit
     * @return array of messages data.
     * @throws StorageException
     */
    @Sql("SELECT tc.lastpost_id FROM topic_cache tc WHERE tc.forum_id > 0 AND tc.ignored = FALSE ORDER BY tc.lastpost_date DESC LIMIT ?")
    int[] getLatestTopics(int limit) throws StorageException;

    /**
     * Returns messages data for specified forum id.
     *
     * @param forumId target forum id
     * @param userId
     * @return array of topic messages data.
     * @throws StorageException
     */
    @Sql("SELECT\n" +
                 "  m.id,\n" +
                 "  m.topic_id,\n" +
                 "  m.parent_id,\n" +
                 "  m.forum_id,\n" +
                 "  m.user_id,\n" +
                 "  m.subject,\n" +
                 "  m.user_nick,\n" +
                 "  m.message_date,\n" +
                 "  m.update_date,\n" +
                 "  m.`read`,\n" +
                 "  m.rating,\n" +
                 "  tc.ignored,\n" +
                 "  m.parent_user_id,\n" +
                 "  CASEWHEN(m.user_id = 0, FALSE, (\n" +
                 "    SELECT\n" +
                 "  iu.ignored\n" +
                 "    FROM user iu\n" +
                 "    WHERE iu.id = m.user_id))        AS ignoreduser,\n" +
                 "  tc.replies,\n" +
                 "  CASEWHEN(tc.ignored, 0, (\n" +
                 "    SELECT\n" +
                 "  count(um.id)\n" +
                 "    FROM message um\n" +
                 "    WHERE um.topic_id = m.id AND um.`read` = FALSE AND um.forum_id = m.forum_id AND NOT CASEWHEN(um.user_id = 0, FALSE, (\n" +
                 "      SELECT\n" +
                 "        u.ignored\n" +
                 "      FROM user u\n" +
                 "      WHERE u.id = um.user_id))))    AS unreadmessages,\n" +
                 "  CASEWHEN(tc.ignored, 0, (\n" +
                 "    SELECT\n" +
                 "  count(m1.id)\n" +
                 "    FROM message m1\n" +
                 "    WHERE NOT CASEWHEN(m1.user_id = 0, FALSE, (\n" +
                 "      SELECT\n" +
                 "        u.ignored\n" +
                 "      FROM user u\n" +
                 "      WHERE u.id = m1.user_id)) AND m1.read = FALSE AND m1.parent_user_id <> m1.user_id AND m1.topic_id = m.id AND m1.parent_user_id = ? AND\n" +
                 "          m1.forum_id = m.forum_id)) AS unreadreplies,\n" +
                 "  tc.lastpost_date\n" +
                 "FROM topic_cache tc JOIN message m\n" +
                 "    ON (tc.topic_id = m.id)\n" +
                 "WHERE tc.forum_id = ?")
    @ToObjectConverter(ToThreadDataConverter.class)
    List<ItemStatisticData<MessageData>> getTopicMessagesDataByForumId(int forumId, int userId) throws StorageException;

    /**
     * Returns all a user posts.
     *
     * @param userId user to search.
     * @return user posts.
     * @throws StorageException
     */
    @Sql("SELECT id, topic_id, parent_id, forum_id, user_id, subject, user_nick, message_date, update_date, read, rating, (SELECT tc.ignored FROM topic_cache tc WHERE tc.topic_id = CASEWHEN(m.topic_id = 0, m.id, m.topic_id)) AS ignored, parent_user_id, CASEWHEN(m.user_id = 0, FALSE, (SELECT iu.ignored FROM user iu WHERE iu.id = m.user_id)) AS ignoreduser FROM message m WHERE user_id = ? AND forum_id > 0")
    @MapRowTo(MessageData.class)
    List<MessageData> getUserPosts(int userId) throws StorageException;

    /**
     * Returns all replies on the user post
     *
     * @param userId
     * @return
     * @throws StorageException
     */
    @Sql("SELECT m.id, m.topic_id, m.parent_id, m.forum_id, m.user_id, m.subject, m.user_nick, m.message_date, m.update_date, m.read, m.rating, (SELECT tc.ignored FROM topic_cache tc WHERE tc.topic_id = CASEWHEN(m.topic_id = 0, m.id, m.topic_id)), m.parent_user_id, FALSE AS ignoreduser FROM message m WHERE NOT CASEWHEN(m.user_id = 0, FALSE, (SELECT iu.ignored FROM user iu WHERE iu.id = m.user_id)) AND (SELECT tc.ignored FROM topic_cache tc WHERE tc.topic_id = CASEWHEN(m.topic_id = 0, m.id, m.topic_id)) = FALSE AND m.user_id <> m.parent_user_id AND m.parent_user_id = ? AND m.forum_id > 0")
    @MapRowTo(MessageData.class)
    List<MessageData> getUserReplies(int userId) throws StorageException;

    @Sql("SELECT id, topic_id, parent_id, forum_id, user_id, subject, user_nick, message_date, update_date, read, rating,(SELECT tc.ignored FROM topic_cache tc WHERE tc.topic_id = CASEWHEN(m.topic_id = 0, m.id, m.topic_id)), parent_user_id, CASEWHEN(m.user_id = 0, FALSE, (SELECT iu.ignored FROM user iu WHERE iu.id = m.user_id)) AS ignoreduser FROM message m WHERE id = ?")
    MessageData getMessageData(int messageId) throws StorageException;

    /**
     * Mark the whole thread as read/unread.
     *
     * @param topicId target topic id.
     * @param read    new read state.
     * @throws StorageException
     */
    @Sql("UPDATE message SET read=? WHERE topic_id=?")
    void updateThreadReadFlag(int topicId, boolean read) throws StorageException;

    /**
     * Updates specified message rating cache. See {@linkplain org.xblackcat.rojac.data.RatingCache} for details.
     *
     * @param id           target message id
     * @param ratingsCache new cached ratings string.
     * @throws StorageException
     */
    @Sql("UPDATE message SET rating=? WHERE id=?")
    void updateMessageRatingCache(int id, String ratingsCache) throws StorageException;

//    SetReadExPacket setThreadReadBeforeDate(long dateline, boolean read, int forumId, int threadId) throws StorageException;
//
//    SetReadExPacket setThreadReadAfterDate(long dateline, boolean read, int forumId, int threadId) throws StorageException;
//
//    SetReadExPacket setForumReadBeforeDate(long dateline, boolean read, int forumId) throws StorageException;
//
//    SetReadExPacket setForumReadAfterDate(long dateline, boolean read, int forumId) throws StorageException;
//
//    SetReadExPacket setReadBeforeDate(long dateline, boolean read) throws StorageException;
//
//    SetReadExPacket setReadAfterDate(long dateline, boolean read) throws StorageException;

    @Sql("SELECT id, m.topic_id, parent_id, m.forum_id, user_id, subject, user_nick, message_date, update_date, read, rating, TRUE AS ignored, parent_user_id,FALSE AS ignoreduser FROM topic_cache tc JOIN message m ON (tc.topic_id = m.id) WHERE tc.ignored = TRUE")
    @MapRowTo(MessageData.class)
    List<MessageData> getIgnoredTopicsList() throws StorageException;

    @Sql("SELECT m.id FROM message m WHERE NOT CASEWHEN(m.user_id = 0, FALSE, (SELECT iu.ignored FROM user iu WHERE iu.id = m.user_id)) AND (SELECT tc.ignored FROM topic_cache tc WHERE tc.topic_id = CASEWHEN(m.topic_id = 0, m.id, m.topic_id)) = FALSE AND m.read = FALSE AND m.parent_user_id <> m.user_id AND m.parent_user_id = ? AND m.forum_id > 0 ORDER BY m.message_date LIMIT 1")
    Integer getFirstUnreadReply(int userId) throws StorageException;

    @Sql("UPDATE topic_cache tc SET tc.replies = ((SELECT count(mm.id) FROM message mm WHERE mm.topic_id = tc.topic_id AND mm.forum_id = tc.forum_id)) WHERE tc.topic_id = ?")
    void updateRepliesAmountInfo(int threadId) throws StorageException;

    @Sql("MERGE INTO topic_cache (topic_id, forum_id, ignored, lastpost_id, replies) SELECT m.id,m.forum_id,IFNULL((SELECT tco.ignored FROM topic_cache tco WHERE tco.topic_id = m.topic_id), FALSE) AS ignored, ((SELECT max(mm.id) FROM message mm WHERE mm.topic_id = m.id AND mm.forum_id = m.forum_id)) AS lastpost_id,((SELECT count(mr.id) FROM message mr WHERE mr.topic_id = m.id AND mr.forum_id = m.forum_id)) AS replies FROM message m WHERE m.id = ?")
    void updateLastPostId(int threadId) throws StorageException;

    @Sql("UPDATE topic_cache tc SET tc.lastpost_date = IFNULL((SELECT mm.message_date FROM message mm WHERE mm.id = tc.lastpost_id), (SELECT m.message_date FROM message m WHERE m.id = tc.topic_id)) WHERE tc.topic_id = ?")
    void updateLastPostDate(int threadId) throws StorageException;

    @Sql("UPDATE message m SET m.parent_user_id = IFNULL((SELECT pm.user_id FROM message pm WHERE pm.id = m.parent_id AND m.forum_id = pm.forum_id), 0) WHERE m.id = ?")
    void updateParentPostUserId(int messageId) throws StorageException;
}
