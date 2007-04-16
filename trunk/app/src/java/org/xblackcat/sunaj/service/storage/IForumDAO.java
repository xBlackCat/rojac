package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.Forum;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IForumDAO {
    void storeForum(Forum f) throws StorageException;

    boolean removeForum(int id) throws StorageException;

    Forum getForumById(int forumId) throws StorageException;

    int[] getForumIdsInGroup(int forumGroupId) throws StorageException;

    int[] getAllForumIds() throws StorageException;
}
