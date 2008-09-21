package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Forum;

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

    Forum[] getAllForums() throws StorageException;

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

    int getMessagesInForum(int forumId) throws StorageException;

    int getUnreadMessagesInForum(int forumId) throws StorageException;
}
