package org.xblackcat.rojac.data.favorite;

import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * @author xBlackCat
 */

class UnreadUserPostsFavorite extends AnItemFavorite {
    UnreadUserPostsFavorite(Integer id, String config) {
        super(id, config);
    }

    @Override
    public FavoriteType getFavoriteType() {
        return FavoriteType.UnreadUserPosts;
    }

    @Override
    protected int loadAmount() throws StorageException {
        return ServiceFactory.getInstance().getStorage().getMessageAH().getUnreadUserPosts(itemId);
    }
}