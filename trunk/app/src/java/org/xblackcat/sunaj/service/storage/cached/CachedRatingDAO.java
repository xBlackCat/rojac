package org.xblackcat.sunaj.service.storage.cached;

import org.xblackcat.sunaj.service.data.Rating;
import org.xblackcat.sunaj.service.storage.IRatingDAO;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 16.04.2007
*
* @author ASUS
*/
final class CachedRatingDAO implements IRatingDAO, IPurgable {
    private final Cache<Rating[]> ratingsForMessageCache = new Cache<Rating[]>();

    private final IRatingDAO storage;

    CachedRatingDAO(IRatingDAO storage) {
        this.storage = storage;
    }

    public void storeRating(Rating ri) throws StorageException {
        storage.storeRating(ri);
        ratingsForMessageCache.remove(ri.getMessageId());
    }

    public boolean removeRatingsByMessageId(int messageId) throws StorageException {
        if (storage.removeRatingsByMessageId(messageId)) {
            ratingsForMessageCache.remove(messageId);
            return true;
        } else {
            return false;
        }
    }

    public Rating[] getRatingsByMessageId(int messageId) throws StorageException {
        Rating[] ratings = ratingsForMessageCache.get(messageId);
        if (ratings == null) {
            ratings = storage.getRatingsByMessageId(messageId);
            if (ratings != null) {
                ratingsForMessageCache.put(messageId, ratings);
            }
        }
        return ratings;
    }

    public Rating[] getAllRatings() throws StorageException {
        return storage.getAllRatings();
    }

    public void purge() {
        ratingsForMessageCache.purge();
    }
}
