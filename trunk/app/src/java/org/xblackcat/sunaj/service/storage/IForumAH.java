package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.data.Forum;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IForumAH extends AH {
    void storeForum(Forum f) throws StorageException;

    boolean removeForum(int id) throws StorageException;

    Forum getForumById(int forumId) throws StorageException;

    int[] getForumIdsInGroup(int forumGroupId) throws StorageException;

    int[] getAllForumIds() throws StorageException;

    int[] getSubscribedForumIds() throws StorageException;

    /**
     * Updates forum information. Notice that <code>isSubscribed</code>  field is not changed during operation.
     *
     * @param f
     *
     * @throws StorageException
     */
    void updateForum(Forum f) throws StorageException;

    void setSubscribeForum(int forumId, boolean subscribe) throws StorageException;
}