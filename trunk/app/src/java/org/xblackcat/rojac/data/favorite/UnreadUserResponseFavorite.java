package org.xblackcat.rojac.data.favorite;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * @author xBlackCat
 */

class UnreadUserResponseFavorite extends AnItemFavorite {
    UnreadUserResponseFavorite(Integer id, String config) {
        super(id, config);
    }

    @Override
    public FavoriteType getType() {
        return FavoriteType.UnreadUserResponses;
    }

    @Override
    protected int loadAmount() throws StorageException {
        return ServiceFactory.getInstance().getStorage().getMessageAH().getUnreadReplies(itemId);
    }

    @Override
    protected String loadName() throws StorageException {
        return "User #" + id + " responses";
    }
}
