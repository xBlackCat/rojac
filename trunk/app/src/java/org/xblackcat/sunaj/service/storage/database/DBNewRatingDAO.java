package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.service.data.NewRating;
import org.xblackcat.sunaj.service.storage.INewRatingDAO;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.ToNewRatingConvertor;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

final class DBNewRatingDAO implements INewRatingDAO {
    private final IQueryExecutor helper;

    DBNewRatingDAO(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeNewRating(NewRating nr) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_NEW_RATING,
                nr.getId(),
                nr.getMessageId(),
                nr.getRate());
    }

    public boolean removeNewRating(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_NEW_RATING, id) > 0;
    }

    public NewRating getNewRating(int id) throws StorageException {
        return helper.executeSingle(new ToNewRatingConvertor(), DataQuery.GET_OBJECT_NEW_RATING, id);
    }

    public int[] getNewRatingIdsByMessageId(int messageId) throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_NEW_RATING_BY_MESSAGE_ID, messageId);
    }

    public int[] getAllNewRatingIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_NEW_RATING);
    }

    public void clearRatings() throws StorageException {
        helper.update(DataQuery.REMOVE_ALL_OBJECTS_NEW_RATING);
    }
}
