package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.MarkStat;
import org.xblackcat.rojac.data.Rating;
import ru.rsdn.Janus.JanusRatingInfo;

import java.util.Collection;

/**
 * @author ASUS
 */

public interface IRatingAH extends AH {
    void storeRating(JanusRatingInfo ri) throws StorageException;

    Rating[] getRatingsByMessageId(int messageId) throws StorageException;

    Collection<MarkStat> getMarkStatByMessageId(int messageId) throws StorageException;
}
