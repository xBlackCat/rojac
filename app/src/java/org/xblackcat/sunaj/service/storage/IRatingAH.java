package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.Mark;
import org.xblackcat.sunaj.service.data.Rating;

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
