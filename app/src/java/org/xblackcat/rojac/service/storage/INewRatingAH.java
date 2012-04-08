package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;

/**
 * @author ASUS
 */

public interface INewRatingAH extends AH {
    void storeNewRating(int messageId, Mark rate) throws StorageException;

    boolean removeNewRating(int id) throws StorageException;

    Iterable<NewRating> getNewRatingsByMessageId(int messageId) throws StorageException;

    Iterable<NewRating> getAllNewRatings() throws StorageException;

    void clearRatings() throws StorageException;

    Iterable<Mark> getNewRatingMarksByMessageId(int messageId) throws StorageException;
}
