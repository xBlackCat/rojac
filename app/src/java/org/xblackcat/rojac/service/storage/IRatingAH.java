package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.MarkStat;
import org.xblackcat.rojac.data.Rating;
import ru.rsdn.Janus.JanusRatingInfo;

import java.util.List;

/**
 * @author ASUS
 */

public interface IRatingAH extends AH {
    void storeRating(JanusRatingInfo ri) throws StorageException;

    boolean removeRatingsByMessageId(int messageId) throws StorageException;

    Rating[] getRatingsByMessageId(int messageId) throws StorageException;

    Rating[] getAllRatings() throws StorageException;

    Mark[] getRatingMarksByMessageId(int messageId) throws StorageException;

    List<MarkStat> getMarkStatByMessageId(int messageId) throws StorageException;
}
