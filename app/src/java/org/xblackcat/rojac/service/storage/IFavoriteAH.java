package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.gui.view.favorites.FavoriteType;
import org.xblackcat.rojac.gui.view.favorites.IFavorite;

/**
 * @author xBlackCat
 */

public interface IFavoriteAH {
    IFavorite[] getFavorites() throws StorageException;

    IFavorite createFavorite(String name, FavoriteType type, String config) throws StorageException;

    void removeFavorite(int id) throws StorageException;
}
