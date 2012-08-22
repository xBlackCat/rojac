package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.MarkStat;
import org.xblackcat.rojac.data.Rating;
import ru.rsdn.janus.JanusRatingInfo;

/**
 * @author ASUS
 */

public interface IRatingAH extends AH {
    void storeRating(JanusRatingInfo ri) throws StorageException;

    org.xblackcat.rojac.service.storage.IResult<Rating> getRatingsByMessageId(int messageId) throws StorageException;

    org.xblackcat.rojac.service.storage.IResult<MarkStat> getMarkStatByMessageId(int messageId) throws StorageException;
}
