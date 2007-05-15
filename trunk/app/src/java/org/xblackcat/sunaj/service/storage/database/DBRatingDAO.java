package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.service.data.Rating;
import org.xblackcat.sunaj.service.storage.IRatingDAO;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.ToRatingConvertor;

import java.util.Collection;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

final class DBRatingDAO implements IRatingDAO {
    private final IQueryExecutor helper;

    DBRatingDAO(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeRating(Rating r) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_RATING,
                r.getMessageId(),
                r.getTopicId(),
                r.getUserId(),
                r.getUserRating(),
                r.getRate(),
                r.getRateDate());
    }

    public boolean removeRatingsByMessageId(int messageId) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECTS_RATING, messageId) > 0;
    }

    public Rating[] getRatingsByMessageId(int messageId) throws StorageException {
        Collection<Rating> ratings = helper.execute(new ToRatingConvertor(), DataQuery.GET_OBJECTS_RATING_BY_MESSAGE_ID, messageId);
        return ratings.toArray(new Rating[ratings.size()]);
    }

    public Rating[] getAllRatings() throws StorageException {
        Collection<Rating> ratings = helper.execute(new ToRatingConvertor(), DataQuery.GET_OBJECTS_RATING);
        return ratings.toArray(new Rating[ratings.size()]);
    }
}
