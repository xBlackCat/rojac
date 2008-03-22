package org.xblackcat.sunaj.service.storage.cached;

import org.xblackcat.sunaj.data.Mark;
import org.xblackcat.sunaj.data.Rating;
import org.xblackcat.sunaj.service.storage.IRatingAH;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 16.04.2007
*
* @author ASUS
*/
final class CachedRatingAH implements IRatingAH, IPurgable {
    private final Cache<Rating[]> ratingsForMessageCache = new Cache<Rating[]>();

    private final IRatingAH storage;

    CachedRatingAH(IRatingAH storage) {
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

    public Mark[] getRatingMarksByMessageId(int messageId) throws StorageException {
        return storage.getRatingMarksByMessageId(messageId);
    }

    public void purge() {
        ratingsForMessageCache.purge();
    }
}
