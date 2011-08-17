package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.MarkStat;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.service.storage.IRatingAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;
import ru.rsdn.Janus.JanusRatingInfo;

import java.util.Collection;

/**
 * @author ASUS
 */

final class DBRatingAH extends AnAH implements IRatingAH {
    DBRatingAH(IQueryExecutor helper) {
        super(helper);
    }

    public void storeRating(JanusRatingInfo r) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_RATING,
                r.getMessageId(),
                r.getTopicId(),
                r.getUserId(),
                r.getUserRating(),
                Mark.getMark(r.getRate()).getValue(),
                r.getRateDate().getTimeInMillis());
    }

    public Rating[] getRatingsByMessageId(int messageId) throws StorageException {
        Collection<Rating> ratings = helper.execute(Converters.TO_RATING, DataQuery.GET_OBJECTS_RATING_BY_MESSAGE_ID, messageId);
        return ratings.toArray(new Rating[ratings.size()]);
    }

    public Collection<MarkStat> getMarkStatByMessageId(int messageId) throws StorageException {
        return helper.execute(
                Converters.TO_MARK_STAT,
                DataQuery.GET_OBJECTS_MARK_STAT_BY_MESSAGE_ID,
                messageId
        );
    }
}
