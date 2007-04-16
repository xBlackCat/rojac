package org.xblackcat.sunaj.service.storage.cached;

import org.xblackcat.sunaj.service.data.NewRating;
import org.xblackcat.sunaj.service.storage.INewRatingDAO;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 16.04.2007
*
* @author ASUS
*/
final class CachedNewRatingDAO implements INewRatingDAO,IPurgable {
    private final INewRatingDAO newRatingDAO;

    CachedNewRatingDAO(INewRatingDAO newRatingDAO) {
        this.newRatingDAO = newRatingDAO;
    }

    public void storeNewRating(NewRating nr) throws StorageException {
        newRatingDAO.storeNewRating(nr);
    }

    public boolean removeNewRating(int id) throws StorageException {
        return newRatingDAO.removeNewRating(id);
    }

    public NewRating getNewRating(int id) throws StorageException {
        return newRatingDAO.getNewRating(id);
    }

    public int[] getAllNewRatingIds() throws StorageException {
        return newRatingDAO.getAllNewRatingIds();
    }

    public void purge() {
    }
}
