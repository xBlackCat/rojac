package org.xblackcat.rojac.service.storage.database;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.set.hash.TIntHashSet;
import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.rojac.service.IProgressTracker;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;
import ru.rsdn.Janus.RequestForumInfo;

import java.util.ArrayList;
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
    public org.xblackcat.rojac.service.storage.IResult<RequestForumInfo> getSubscribedForums() throws StorageException {
        return helper.execute(Converters.TO_SUBSCRIBED_FORUM, DataQuery.GET_SUBSCRIBED_FORUMS);
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
    public org.xblackcat.rojac.service.storage.IResult<ItemStatisticData<Forum>> getAllForums(int userId) throws StorageException {
        return helper.execute(Converters.TO_FORUM_DATA, DataQuery.GET_OBJECTS_FORUM, userId);
    }

    @Override
    public DiscussionStatistic getForumStatistic(int forumId, int userId) throws StorageException {
        return helper.executeSingle(Converters.TO_DISCUSSION_STATISTIC, DataQuery.GET_FORUM_STATISTIC, userId, forumId);
    }

    @Override
    public void updateForumStatistic(IProgressTracker tracker, TIntHashSet forumIds) throws StorageException {
        Collection<Object[]> params = new ArrayList<>();

        TIntIterator iterator = forumIds.iterator();
        while (iterator.hasNext()) {
            params.add(new Integer[]{iterator.next()});
        }

        helper.updateBatch(DataQuery.UPDATE_FORUM_SET_STAT, tracker, params);
    }

    @Override
    public boolean hasSubscribedForums() throws StorageException {
        return true;
    }
}
