package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.ForumGroup;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.Sql;

/**
 * @author ASUS
 */

public interface IForumGroupAH extends IAH {
    @Sql("INSERT INTO forum_group (id, name, sort_order) VALUES (?, ?, ?)")
    void storeForumGroup(int forumGroupId, String forumGroupName, int sortOrder) throws StorageException;

    @Sql("DELETE FROM forum_group WHERE id=?")
    boolean removeForumGroup(int id) throws StorageException;

    @Sql("SELECT id, name, sort_order FROM forum_group WHERE id=?")
    ForumGroup getForumGroupById(int forumGroupId) throws StorageException;

    @Sql("UPDATE forum_group SET name=?, sort_order=? WHERE id=?")
    void updateForumGroup(String forumGroupName, int sortOrder, int forumGroupId) throws StorageException;
}
