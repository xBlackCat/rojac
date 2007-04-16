package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.Rating;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IRatingDAO {
    void storeRating(Rating ri) throws StorageException;

    boolean removeRating(int id) throws StorageException;

    Rating getRatingById(int id) throws StorageException;

    int[] getRatingIdsByMessageId(int messageId) throws StorageException;

    int[] getAllRatingIds() throws StorageException;
}
