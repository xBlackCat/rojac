package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

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
    public boolean removeForum(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_FORUM, id) > 0;
    }

    @Override
    public Forum getForumById(int forumId) throws StorageException {
        return helper.executeSingle(Converters.TO_FORUM_CONVERTER, DataQuery.GET_OBJECT_FORUM, forumId);
    }

    @Override
    public int[] getForumIdsInGroup(int forumGroupId) throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_FORUM_BY_FORUM_GROUP, forumGroupId);
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
    public int getMessagesInForum(int forumId) throws StorageException {
        return helper.executeSingle(Converters.TO_INTEGER_CONVERTER,
                DataQuery.GET_MESSAGES_NUMBER_IN_FORUM,
                forumId);
    }

    @Override
    public int getUnreadMessagesInForum(int forumId) throws StorageException {
        return helper.executeSingle(Converters.TO_INTEGER_CONVERTER,
                DataQuery.GET_UNREAD_MESSAGES_NUMBER_IN_FORUM,
                forumId);
    }

    @Override
    public Long getLastMessageDateInForum(int forumId) throws StorageException {
        return helper.executeSingle(Converters.TO_LONG_CONVERTER,
                DataQuery.GET_LAST_MESSAGE_DATE_IN_FORUM,
                forumId);
    }

    @Override
    public int[] getAllForumIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_FORUM);
    }
}
