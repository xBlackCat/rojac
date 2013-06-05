package org.xblackcat.rojac.service.storage;

import gnu.trove.set.hash.TIntHashSet;
import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.rojac.service.IProgressTracker;
import ru.rsdn.janus.RequestForumInfo;

/**
 * @author ASUS
 */

public interface IForumAH extends AH {
    void storeForum(
            int forumId,
            int forumGroupId,
            int rated,
            int inTop,
            int rateLimit,
            boolean subscribed,
            String shortForumName, String forumName
    ) throws StorageException;

    Forum getForumById(int forumId) throws StorageException;

    IResult<RequestForumInfo> getSubscribedForums() throws StorageException;

    /**
     * Updates forum information. Notice that <code>isSubscribed</code>  field is not changed during operation.
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
    void updateForum(
            int forumGroupId,
            int rated,
            int inTop,
            int rateLimit,
            String shortForumName,
            String forumName, int forumId
    ) throws StorageException;

    void setSubscribeForum(int forumId, boolean subscribe) throws StorageException;

    void setForumRead(int forumId, boolean read) throws StorageException;

    /**
     * Returns list of all available forums.
     *
     * @return list of all available forums.
     * @throws StorageException
     */
    IResult<ItemStatisticData<Forum>> getAllForums() throws StorageException;

    DiscussionStatistic getForumStatistic(int forumId, int userId) throws StorageException;

    void updateForumStatistic(IProgressTracker tracker, TIntHashSet forums) throws StorageException;

    boolean hasSubscribedForums() throws StorageException;

    IResult<Forum> getForumLists() throws StorageException;

    void updateForumStatistic(int forumId) throws StorageException;
}
