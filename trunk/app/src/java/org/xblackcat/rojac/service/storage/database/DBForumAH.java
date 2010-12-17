package org.xblackcat.rojac.service.storage.database;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

import java.util.Collection;
import java.util.Map;

/**
 * @author ASUS
 */
final class DBForumAH implements IForumAH {
    private final IQueryExecutor helper;

    DBForumAH(IQueryExecutor helper) {
        this.helper = helper;
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
    public Map<Integer, Number> getMessagesInForum(int... forumIds) throws StorageException {
        return helper.executeSingleBatch(Converters.TO_NUMBER,
                DataQuery.GET_MESSAGES_NUMBER_IN_FORUM,
                ArrayUtils.toObject(forumIds));
    }

    @Override
    public Map<Integer, Number> getUnreadMessagesInForum(int... forumIds) throws StorageException {
        return helper.executeSingleBatch(Converters.TO_NUMBER,
                DataQuery.GET_UNREAD_MESSAGES_NUMBER_IN_FORUM,
                ArrayUtils.toObject(forumIds));
    }

    @Override
    public Map<Integer, Number> getLastMessageDateInForum(int... forumIds) throws StorageException {
        return helper.executeSingleBatch(Converters.TO_NUMBER,
                DataQuery.GET_LAST_MESSAGE_DATE_IN_FORUM,
                ArrayUtils.toObject(forumIds));
    }

    @Override
    public Collection<Forum> getAllForums() throws StorageException {
        return helper.execute(Converters.TO_FORUM, DataQuery.GET_OBJECTS_FORUM);
    }
}
