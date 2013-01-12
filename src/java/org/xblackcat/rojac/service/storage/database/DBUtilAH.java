package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.IUtilAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
 * @author ASUS
 */

final class DBUtilAH extends AnAH implements IUtilAH {
    DBUtilAH(IQueryHolder helper) {
        super(helper);
    }

    @Override
    public void updateForumsStat() throws StorageException {
        helper.update(DataQuery.CACHE_UPDATE_FORUMS_STAT);
    }

    @Override
    public void updateThreadsStat() throws StorageException {
        helper.update(DataQuery.CACHE_UPDATE_THREADS_STAT);
    }

    @Override
    public void updateLastPostId() throws StorageException {
        helper.update(DataQuery.CACHE_UPDATE_LASTPOST_ID);
    }

    @Override
    public void updateLastPostDate() throws StorageException {
        helper.update(DataQuery.CACHE_UPDATE_LASTPOST_DATE);
    }

    @Override
    public void updateParentUserId() throws StorageException {
        helper.update(DataQuery.CACHE_UPDATE_PARENT_USER_ID);
    }

    @Override
    public IResult<Integer> getTopicIdsToClean(long timeLine, boolean onlyRead, boolean onlyIgnored) throws StorageException {
        return helper.execute(
                Converters.TO_INTEGER,
                DataQuery.GET_OLD_TOPIC_IDS,
                timeLine,
                !onlyRead,
                !onlyIgnored
        );
    }

    @Override
    public void removeTopic(int topicId) throws StorageException {
        helper.update(DataQuery.REMOVE_TOPIC_MODERATES, topicId);
        helper.update(DataQuery.REMOVE_TOPIC_RATING, topicId);
        helper.update(DataQuery.REMOVE_WHOLE_TOPIC, topicId);
    }

    @Override
    public int getTopicsAmountToClean(long timeLine, boolean onlyRead, boolean onlyIgnored) throws StorageException {
        return helper.executeSingle(
                Converters.TO_NUMBER,
                DataQuery.GET_OLD_TOPIC_AMOUNT,
                timeLine,
                !onlyRead,
                !onlyIgnored
        ).intValue();
    }
}
