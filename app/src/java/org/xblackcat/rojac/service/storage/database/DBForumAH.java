package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

import java.util.Collection;

/**
 * @author ASUS
 */
final class DBForumAH extends AnAH implements IForumAH {
    DBForumAH(IQueryHolder helper) {
        super(helper);
    }

    @Override
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

    @Override
    public Forum getForumById(int forumId) throws StorageException {
        return helper.executeSingle(Converters.TO_FORUM, DataQuery.GET_OBJECT_FORUM, forumId);
    }

    @Override
    public int[] getSubscribedForumIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_FORUM_SUBSCRIBED);
    }

    @Override
    public void updateForum(Forum f) throws StorageException {
        helper.update(DataQuery.UPDATE_OBJECT_FORUM,
                f.getForumGroupId(),
                f.getRated(),
                f.getInTop(),
                f.getRateLimit(),
                f.getShortForumName(),
                f.getForumName(),
                f.getForumId());
    }

    @Override
    public void setSubscribeForum(int forumId, boolean subscribe) throws StorageException {
        helper.update(DataQuery.UPDATE_OBJECT_FORUM_SUBSCRIBE,
                subscribe,
                forumId);
    }

    @Override
    public void setForumRead(int forumId, boolean read) throws StorageException {
        helper.update(DataQuery.UPDATE_FORUM_MESSAGES_READ_FLAG,
                read,
                forumId);
    }

    @Override
    public Collection<Forum> getAllForums() throws StorageException {
        return helper.execute(Converters.TO_FORUM, DataQuery.GET_OBJECTS_FORUM);
    }

    @Override
    public Number getMessagesInForum(int forumId) throws StorageException {
        return helper.executeSingle(Converters.TO_NUMBER,
                DataQuery.GET_MESSAGES_NUMBER_IN_FORUM,
                forumId);
    }

    @Override
    public ForumStatistic getForumStatistic(int forumId, int userId) throws StorageException {
        return helper.executeSingle(Converters.TO_FORUM_STATISTIC, DataQuery.GET_FORUM_STATISTIC, userId, forumId);
    }

}
