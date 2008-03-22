package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.data.ForumGroup;
import org.xblackcat.sunaj.service.storage.IForumGroupAH;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.Converters;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

final class DBForumGroupAH implements IForumGroupAH {
    private final IQueryExecutor helper;

    DBForumGroupAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeForumGroup(ForumGroup fg) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_FORUM_GROUP,
                fg.getForumGroupId(),
                fg.getForumGroupName(),
                fg.getSortOrder());
    }

    public boolean removeForumGroup(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_FORUM_GROUP, id) > 0;
    }

    public ForumGroup getForumGroupById(int forumGroupId) throws StorageException {
        return helper.executeSingle(Converters.TO_FORUM_GROUP_CONVERTER, DataQuery.GET_OBJECT_FORUM_GROUP, forumGroupId);
    }

    public int[] getAllForumGroupIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_FORUM_GROUP);
    }
}
