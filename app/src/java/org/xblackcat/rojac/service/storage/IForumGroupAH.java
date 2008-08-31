package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.ForumGroup;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IForumGroupAH extends AH {
    void storeForumGroup(ForumGroup fg) throws StorageException;

    boolean removeForumGroup(int id) throws StorageException;

    ForumGroup getForumGroupById(int forumGroupId) throws StorageException;

    int[] getAllForumGroupIds() throws StorageException;

    void updateForumGroup(ForumGroup fg) throws StorageException;
}
