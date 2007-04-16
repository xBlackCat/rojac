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
    private final Cache<Rating> ratingCache = new Cache<Rating>();
    private final Cache<int[]> ratingsForMessageCache = new Cache<int[]>();

    private final IRatingDAO storage;

    CachedRatingDAO(IRatingDAO storage) {
        this.storage = storage;
    }

    public void storeRating(Rating ri) throws StorageException {
        storage.storeRating(ri);
        ratingsForMessageCache.remove(ri.getMessageId());
    }

    public boolean removeRating(int id) throws StorageException {
        if (storage.removeRating(id)) {
            ratingCache.remove(id);
            return true;
        } else {
            return false;
        }
    }

    public Rating getRatingById(int id) throws StorageException {
        Rating rating = ratingCache.get(id);
        if (rating == null) {
            rating = storage.getRatingById(id);
            if (rating != null) {
                ratingCache.put(id, rating);
            }
        }
        return rating;
    }

    public int[] getRatingIdsByMessageId(int messageId) throws StorageException {
        int[] ratings = ratingsForMessageCache.get(messageId);
        if (ratings == null) {
            ratings = storage.getRatingIdsByMessageId(messageId);
            if (ratings != null) {
                ratingsForMessageCache.put(messageId, ratings);
            }
        }
        return ratings;
    }

    public int[] getAllRatingIds() throws StorageException {
        return storage.getAllRatingIds();
    }

    public void purge() {
        ratingCache.purge();
        ratingsForMessageCache.purge();
    }
}
