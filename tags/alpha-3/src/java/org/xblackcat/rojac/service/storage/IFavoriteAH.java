package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.gui.view.model.FavoriteType;

import java.util.Collection;

/**
 * @author xBlackCat
 */

public interface IFavoriteAH {
    /**
     * Returns all the created favorites.
     *
     * @return array of favorites object.
     *
     * @throws StorageException
     */
    Collection<IFavorite> getFavorites() throws StorageException;

    /**
     * Store a new favorite in database and return it.
     *
     * @param type   favorite type
     * @param config
     *
     * @return
     *
     * @throws StorageException
     */
    IFavorite createFavorite(FavoriteType type, String config) throws StorageException;

    /**
     * Removes a favorite.
     *
     * @param id favorite id to remove.
     *
     * @throws StorageException
     */
    void removeFavorite(int id) throws StorageException;

    /**
     * Loads a specified favorite by its id
     *
     * @param favoriteId
     *
     * @return favorite object.
     *
     * @throws StorageException
     */
    IFavorite getFavorite(int favoriteId) throws StorageException;
}
