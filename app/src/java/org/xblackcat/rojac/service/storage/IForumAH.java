package org.xblackcat.rojac.service.storage;

import gnu.trove.set.hash.TIntHashSet;
import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.rojac.service.IProgressTracker;
import ru.rsdn.Janus.RequestForumInfo;

import java.util.Collection;

/**
 * @author ASUS
 */

public interface IForumAH extends AH {
    void storeForum(Forum f) throws StorageException;

    Forum getForumById(int forumId) throws StorageException;

    Collection<RequestForumInfo> getSubscribedForums() throws StorageException;

    /**
     * Updates forum information. Notice that <code>isSubscribed</code>  field is not changed during operation.
     *
     * @param f Forum object to store
     * @throws StorageException
     */
    void updateForum(Forum f) throws StorageException;

    void setSubscribeForum(int forumId, boolean subscribe) throws StorageException;

    void setForumRead(int forumId, boolean read) throws StorageException;

    /**
     * Returns list of all available forums.
     *
     * @return list of all available forums.
     * @throws StorageException
     * @param userId
     */
    Collection<ItemStatisticData<Forum>> getAllForums(int userId) throws StorageException;

    DiscussionStatistic getForumStatistic(int forumId, int userId) throws StorageException;

    void updateForumStatistic(IProgressTracker tracker, TIntHashSet forums) throws StorageException;
}
