package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.service.data.Forum;
import org.xblackcat.sunaj.service.storage.IForumAH;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.Converters;

/**
 * Date: 8 трав 2007
 *
 * @author ASUS
 */
final class DBForumAH implements IForumAH {
    private final IQueryExecutor helper;

    DBForumAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeForum(Forum f) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_FORUM,
                f.getForumId(),
                f.getForumGroupId(),
                f.getRated(),
                f.getInTop(),
                f.getRateLimit(),
                f.isSubscribed(),
                f.getShortForumName(),
                f.getForumName());
    }

    public boolean removeForum(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_FORUM, id) > 0;
    }

    public Forum getForumById(int forumId) throws StorageException {
        return helper.executeSingle(Converters.TO_FORUM_CONVERTER, DataQuery.GET_OBJECT_FORUM, forumId);
    }

    public int[] getForumIdsInGroup(int forumGroupId) throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_FORUM_BY_FORUM_GROUP, forumGroupId);
    }

    public int[] getSubscribedForumIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_FORUM_SUBSCRIBED);
    }

    public int[] getAllForumIds() throws StorageException {
        return helper.getIds( DataQuery.GET_IDS_FORUM);
    }
}
