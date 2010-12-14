package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;

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
    public String loadName() throws StorageException {
        return "User #" + itemId + " responses";
    }

    @Override
    public Post getRootNode() {
        assert RojacUtils.checkThread(false, getClass());

        throw new NotImplementedException();
    }
}
