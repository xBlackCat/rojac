package org.xblackcat.rojac.service.storage.cached;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.Rating;
import org.xblackcat.rojac.service.storage.IRatingAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Date: 16.04.2007
*
* @author ASUS
*/
final class CachedRatingAH implements IRatingAH, IPurgable {
    private final Cache<Rating[]> ratingsForMessageCache = new Cache<Rating[]>();

    private final IRatingAH ratingAH;

    CachedRatingAH(IRatingAH ratingAH) {
        this.ratingAH = ratingAH;
    }

    public void storeRating(Rating ri) throws StorageException {
        ratingAH.storeRating(ri);
        ratingsForMessageCache.remove(ri.getMessageId());
    }

    public boolean removeRatingsByMessageId(int messageId) throws StorageException {
        if (ratingAH.removeRatingsByMessageId(messageId)) {
            ratingsForMessageCache.remove(messageId);
            return true;
        } else {
            return false;
        }
    }

    public Rating[] getRatingsByMessageId(int messageId) throws StorageException {
        Rating[] ratings = ratingsForMessageCache.get(messageId);
        if (ratings == null) {
            ratings = ratingAH.getRatingsByMessageId(messageId);
            if (ratings != null) {
                ratingsForMessageCache.put(messageId, ratings);
            }
        }
        return ratings;
    }

    public Rating[] getAllRatings() throws StorageException {
        return ratingAH.getAllRatings();
    }

    public Mark[] getRatingMarksByMessageId(int messageId) throws StorageException {
        return ratingAH.getRatingMarksByMessageId(messageId);
    }

    public void purge() {
        ratingsForMessageCache.purge();
    }
}
