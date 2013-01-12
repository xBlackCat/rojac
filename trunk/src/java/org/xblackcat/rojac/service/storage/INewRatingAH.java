package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;

/**
 * @author ASUS
 */

public interface INewRatingAH extends AH {
    void storeNewRating(int messageId, Mark rate) throws StorageException;

    boolean removeNewRating(int id) throws StorageException;

    org.xblackcat.rojac.service.storage.IResult<NewRating> getNewRatingsByMessageId(int messageId) throws StorageException;

    org.xblackcat.rojac.service.storage.IResult<NewRating> getAllNewRatings() throws StorageException;

    void clearRatings() throws StorageException;

    org.xblackcat.rojac.service.storage.IResult<Mark> getNewRatingMarksByMessageId(int messageId) throws StorageException;
}
