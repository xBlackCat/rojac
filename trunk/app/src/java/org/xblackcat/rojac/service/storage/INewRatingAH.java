package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.NewRating;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface INewRatingAH extends AH {
    void storeNewRating(int messageId, Mark rate) throws StorageException;

    boolean removeNewRating(int id) throws StorageException;

    NewRating[] getNewRatingsByMessageId(int messageId) throws StorageException;

    NewRating[] getAllNewRatings() throws StorageException;

    void clearRatings() throws StorageException;

    Mark[] getNewRatingMarksByMessageId(int messageId) throws StorageException;
}
