package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.service.datahandler.FavoritesUpdatedPacket;
import org.xblackcat.rojac.service.storage.IFavoriteAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

/**
 * @author xBlackCat
 */
public class FavoriteAdder extends RojacWorker<Void, Void> {
    private final FavoriteType type;
    private final int itemId;

    public FavoriteAdder(FavoriteType type, int itemId) {
        this.type = type;
        this.itemId = itemId;
    }

    @Override
    protected Void perform() throws Exception {
        IFavoriteAH favoriteAH = Storage.get(IFavoriteAH.class);

        favoriteAH.createFavorite(type, itemId);
        return null;
    }

    @Override
    protected void done() {
        new FavoritesUpdatedPacket().dispatch();
    }
}
