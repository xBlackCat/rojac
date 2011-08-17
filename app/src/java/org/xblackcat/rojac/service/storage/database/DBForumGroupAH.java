package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.ForumGroup;
import org.xblackcat.rojac.service.storage.IForumGroupAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
 * @author ASUS
 */

final class DBForumGroupAH extends AnAH implements IForumGroupAH {
    public DBForumGroupAH(IQueryExecutor helper) {
        super(helper);
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
        return helper.executeSingle(Converters.TO_FORUM_GROUP, DataQuery.GET_OBJECT_FORUM_GROUP, forumGroupId);
    }

    public int[] getAllForumGroupIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_FORUM_GROUP);
    }

    public void updateForumGroup(ForumGroup fg) throws StorageException {
        helper.update(DataQuery.UPDATE_OBJECT_FORUM_GROUP,
                fg.getForumGroupName(),
                fg.getSortOrder(),
                fg.getForumGroupId());
    }
}
