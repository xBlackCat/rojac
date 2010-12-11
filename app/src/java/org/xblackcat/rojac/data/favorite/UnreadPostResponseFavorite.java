package org.xblackcat.rojac.data.favorite;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * @author xBlackCat
 */

class UnreadPostResponseFavorite extends AnItemFavorite {
    UnreadPostResponseFavorite(Integer id, String config) {
        super(id, config);
    }

    @Override
    public FavoriteType getFavoriteType() {
        return FavoriteType.UnreadPostResponses;
    }

    @Override
    protected int loadAmount() throws StorageException {
        return ServiceFactory.getInstance().getStorage().getMessageAH().getUnreadReplaysInThread(itemId);
    }
}
