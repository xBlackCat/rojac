package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.Rating;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IRatingAH extends AH {
    void storeRating(Rating ri) throws StorageException;

    boolean removeRatingsByMessageId(int messageId) throws StorageException;

    Rating[] getRatingsByMessageId(int messageId) throws StorageException;

    Rating[] getAllRatings() throws StorageException;

    Mark[] getRatingMarksByMessageId(int messageId) throws StorageException;
}
