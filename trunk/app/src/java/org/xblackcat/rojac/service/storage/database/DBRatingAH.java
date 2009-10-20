package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.service.storage.IRatingAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

import java.util.Collection;

/**
 * @author ASUS
 */

final class DBRatingAH implements IRatingAH {
    private final IQueryExecutor helper;

    DBRatingAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeRating(Rating r) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_RATING,
                r.getMessageId(),
                r.getTopicId(),
                r.getUserId(),
                r.getUserRating(),
                r.getRate().getValue(),
                r.getRateDate());
    }

    public boolean removeRatingsByMessageId(int messageId) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECTS_RATING, messageId) > 0;
    }

    public Rating[] getRatingsByMessageId(int messageId) throws StorageException {
        Collection<Rating> ratings = helper.execute(Converters.TO_RATING_CONVERTER, DataQuery.GET_OBJECTS_RATING_BY_MESSAGE_ID, messageId);
        return ratings.toArray(new Rating[ratings.size()]);
    }

    public Rating[] getAllRatings() throws StorageException {
        Collection<Rating> ratings = helper.execute(Converters.TO_RATING_CONVERTER, DataQuery.GET_OBJECTS_RATING);
        return ratings.toArray(new Rating[ratings.size()]);
    }

    public Mark[] getRatingMarksByMessageId(int messageId) throws StorageException {
        Collection<Mark> ratings = helper.execute(
                Converters.TO_MARK_CONVERTER,
                DataQuery.GET_OBJECTS_RATING_MARK_BY_MESSAGE_ID,
                messageId
        );
        return ratings.toArray(new Mark[ratings.size()]);
    }
}
