package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
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
     */
    Collection<Forum> getAllForums() throws StorageException;

    ForumStatistic getForumStatistic(int forumId, int userId) throws StorageException;
}
