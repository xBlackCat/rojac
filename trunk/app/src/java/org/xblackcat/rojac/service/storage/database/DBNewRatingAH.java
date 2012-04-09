package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;
import org.xblackcat.rojac.service.storage.INewRatingAH;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
 * @author ASUS
 */

final class DBNewRatingAH extends AnAH implements INewRatingAH {
    DBNewRatingAH(IQueryHolder helper) {
        super(helper);
    }

    public void storeNewRating(int messageId, Mark rate) throws StorageException {
        long nextId = helper.executeSingle(
                Converters.TO_NUMBER,
                DataQuery.GET_NEXT_ID_NEW_RATING
        ).longValue();
        helper.update(
                DataQuery.STORE_OBJECT_NEW_RATING,
                nextId,
                messageId,
                rate.getValue()
        );
    }

    public boolean removeNewRating(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_NEW_RATING, id) > 0;
    }

    public IResult<NewRating> getNewRatingsByMessageId(int messageId) throws StorageException {
        return helper.execute(
                Converters.TO_NEW_RATING,
                DataQuery.GET_OBJECTS_NEW_RATING_BY_MESSAGE_ID,
                messageId);
    }

    public IResult<NewRating> getAllNewRatings() throws StorageException {
        return helper.execute(Converters.TO_NEW_RATING, DataQuery.GET_OBJECTS_NEW_RATING);
    }

    public void clearRatings() throws StorageException {
        helper.update(DataQuery.REMOVE_ALL_OBJECTS_NEW_RATING);
    }

    public org.xblackcat.rojac.service.storage.IResult<Mark> getNewRatingMarksByMessageId(int messageId) throws StorageException {
        return helper.execute(
                Converters.TO_MARK,
                DataQuery.GET_OBJECTS_NEW_RATING_MARK_BY_MESSAGE_ID,
                messageId
        );
    }
}
