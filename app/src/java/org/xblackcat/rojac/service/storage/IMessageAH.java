package org.xblackcat.rojac.service.storage;

import gnu.trove.set.TIntSet;
import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.IProgressTracker;
import org.xblackcat.rojac.service.datahandler.SetReadExPacket;
import ru.rsdn.Janus.JanusMessageInfo;

/**
 * @author ASUS
 */

public interface IMessageAH extends AH {
    /**
     * Stores a message record by id.
     *
     * @param fm   message data to be stored into database.
     * @param read read state of the message. <code>true</code> - message is read and <code>false</code> otherwise.
     * @throws StorageException
     */
    void storeMessage(JanusMessageInfo fm, boolean read) throws StorageException;

    /**
     * Loads a message body for specified message id.
     *
     * @param messageId id of message body to be loaded.
     * @return the message body.
     * @throws StorageException
     */
    String getMessageBodyById(int messageId) throws StorageException;

    /**
     * Returns a list of topics which roots have not loaded yet.
     *
     * @return topic ids to have been loaded.
     * @throws StorageException
     */
    int[] getBrokenTopicIds() throws StorageException;

    /**
     * Updates a message record by id.
     *
     * @param mes  message data to be stored into database.
     * @param read read state of the message. <code>true</code> - message is read and <code>false</code> otherwise.
     * @throws StorageException
     */
    void updateMessage(JanusMessageInfo mes, boolean read) throws StorageException;

    /**
     * Update last post info in specified threads
     *
     *
     * @param tracker
     * @param threadIds
     * @throws StorageException
     */
    void updateLastPostInfo(IBatchTracker tracker, TIntSet threadIds) throws StorageException;


    void updateParentPostUserId(IProgressTracker tracker, TIntSet messages) throws StorageException;

    /**
     * Updates read flag of the specified message.
     *
     * @param messageId message id to be updated
     * @param read      new state of flag.
     * @throws StorageException
     */
    void updateMessageReadFlag(int messageId, boolean read) throws StorageException;

    /**
     * Checks if a message with specified id is exists.
     *
     * @param messageId message id to check
     * @return <code>true</code> if message already loaded and <code>false</code> elsewise.
     * @throws StorageException
     */
    boolean isExist(int messageId) throws StorageException;

    /**
     * Loads messages of the specified thread.
     *
     * @param threadId
     * @param forumId
     * @return array of messages data.
     * @throws StorageException
     */
    IResult<MessageData> getMessagesDataByTopicId(int threadId, int forumId) throws StorageException;

    /**
     * Loads latest N topics.
     *
     * @param limit
     * @return array of messages data.
     * @throws StorageException
     */
    int[] getLatestTopics(int limit) throws StorageException;

    /**
     * Returns messages data for specified forum id.
     *
     * @param forumId target forum id
     * @param userId
     * @return array of topic messages data.
     * @throws StorageException
     */
    IResult<ItemStatisticData<MessageData>> getTopicMessagesDataByForumId(int forumId, int userId) throws StorageException;

    /**
     * Returns all a user posts.
     *
     * @param userId user to search.
     * @return user posts.
     * @throws StorageException
     */
    IResult<MessageData> getUserPosts(int userId) throws StorageException;

    /**
     * Returns all replies on the user post
     *
     * @param userId
     * @return
     * @throws StorageException
     */
    IResult<MessageData> getUserReplies(int userId) throws StorageException;

    MessageData getMessageData(int messageId) throws StorageException;

    /**
     * Mark the whole thread as read/unread.
     *
     * @param topicId target topic id.
     * @param read    new read state.
     * @throws StorageException
     */
    void updateThreadReadFlag(int topicId, boolean read) throws StorageException;

    /**
     * Updates specified message rating cache. See {@linkplain org.xblackcat.rojac.data.RatingCache} for details.
     *
     * @param id           target message id
     * @param ratingsCache new cached ratings string.
     * @throws StorageException
     */
    void updateMessageRatingCache(int id, String ratingsCache) throws StorageException;

    SetReadExPacket setThreadReadBeforeDate(long dateline, boolean read, int forumId, int threadId) throws StorageException;

    SetReadExPacket setThreadReadAfterDate(long dateline, boolean read, int forumId, int threadId) throws StorageException;

    SetReadExPacket setForumReadBeforeDate(long dateline, boolean read, int forumId) throws StorageException;

    SetReadExPacket setForumReadAfterDate(long dateline, boolean read, int forumId) throws StorageException;

    SetReadExPacket setReadBeforeDate(long dateline, boolean read) throws StorageException;

    SetReadExPacket setReadAfterDate(long dateline, boolean read) throws StorageException;

    IResult<MessageData> getIgnoredTopicsList() throws StorageException;

    Integer getFirstUnreadReply(int userId) throws StorageException;

    void updateLastPostInfo(int threadId) throws StorageException;

    void updateParentPostUserId(int messageId) throws StorageException;
}
