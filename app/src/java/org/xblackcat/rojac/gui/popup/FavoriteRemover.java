package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.service.datahandler.FavoritesUpdatedPacket;
import org.xblackcat.rojac.service.storage.IFavoriteAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

/**
 * @author xBlackCat
 */
class FavoriteRemover extends RojacWorker<Void, Void> {
    private final int favoriteId;

    public FavoriteRemover(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    @Override
    protected Void perform() throws Exception {
        IFavoriteAH fAH = Storage.get(IFavoriteAH.class);

        fAH.removeFavorite(favoriteId);
        return null;
    }

    @Override
    protected void done() {
        new FavoritesUpdatedPacket().dispatch();
    }
}
