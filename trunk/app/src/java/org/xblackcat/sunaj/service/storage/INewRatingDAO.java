package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.NewRating;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface INewRatingDAO {
    void storeNewRating(NewRating nr) throws StorageException;

    boolean removeNewRating(int id) throws StorageException;

    NewRating getNewRating(int id) throws StorageException;

    int[] getAllNewRatingIds() throws StorageException;
}
