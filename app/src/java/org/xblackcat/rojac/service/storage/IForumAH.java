package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Forum;

/**
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

    void setForumRead(int forumId, boolean read) throws StorageException;

    /**
     * Returns total amount of messages in the specified forum.
     *
     * @param forumId forum id.
     *
     * @return total amount of messages in forum.
     *
     * @throws StorageException will be thrown if something wrong.
     */
    int getMessagesInForum(int forumId) throws StorageException;

    /**
     * Returns total amount of unread messages in the specified forum.
     *
     * @param forumId forum id.
     *
     * @return total amount of unread messages in forum.
     *
     * @throws StorageException will be thrown if something wrong.
     */
    int getUnreadMessagesInForum(int forumId) throws StorageException;

    /**
     * Returns last message date in the forum or <code>null</code> if forum is empty.
     *
     * @param forumId forum id.
     *
     * @return last message date in forum or <code>null</code> if forum is empty.
     *
     * @throws StorageException will be thrown if something wrong.
     */
    Long getLastMessageDateInForum(int forumId) throws StorageException;

    /**
     * Returns list of all available forums.
     *
     * @return list of all available forums.
     *
     * @throws StorageException
     */
    Forum[] getAllForums() throws StorageException;
}
