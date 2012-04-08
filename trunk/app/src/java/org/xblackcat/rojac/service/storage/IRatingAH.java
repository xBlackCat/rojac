package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.MarkStat;
import org.xblackcat.rojac.data.Rating;
import ru.rsdn.Janus.JanusRatingInfo;

/**
 * @author ASUS
 */

public interface IRatingAH extends AH {
    void storeRating(JanusRatingInfo ri) throws StorageException;

    Iterable<Rating> getRatingsByMessageId(int messageId) throws StorageException;

    Iterable<MarkStat> getMarkStatByMessageId(int messageId) throws StorageException;
}
