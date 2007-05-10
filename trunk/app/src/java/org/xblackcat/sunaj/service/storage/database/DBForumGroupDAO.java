package org.xblackcat.sunaj.service.storage.database;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.sunaj.service.data.ForumGroup;
import org.xblackcat.sunaj.service.storage.IForumGroupDAO;
import org.xblackcat.sunaj.service.storage.StorageDataException;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.ToForumGroupConvertor;
import org.xblackcat.sunaj.service.storage.database.convert.ToScalarConvertor;

import java.util.Collection;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

class DBForumGroupDAO implements IForumGroupDAO {
    private final IQueryExecutor helper;

    DBForumGroupDAO(IQueryExecutor helper) {
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
        return helper.executeSingle(new ToForumGroupConvertor(), DataQuery.GET_OBJECT_FORUM_GROUP, forumGroupId);
    }

    public int[] getAllForumGroupIds() throws StorageException {
        Collection<Integer> objIds = helper.execute(new ToScalarConvertor<Integer>(), DataQuery.GET_IDS_FORUM_GROUP);
        int[] ids;

        try {
            // Conver collection of Integer to array of int.
            ids = ArrayUtils.toPrimitive(objIds.toArray(new Integer[objIds.size()]));
        } catch (NullPointerException e) {
            throw new StorageDataException("Got null instead of real value.", e);
        }

        return ids;
    }
}
