package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.Rating;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IRatingDAO {
    void storeRating(Rating ri) throws StorageException;

    boolean removeRatingsByMessageId(int messageId) throws StorageException;

    Rating[] getRatingsByMessageId(int messageId) throws StorageException;

    Rating[] getAllRatings() throws StorageException;
}
