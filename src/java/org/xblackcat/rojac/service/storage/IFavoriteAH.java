package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.service.storage.database.convert.ToFavoriteConverter;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.Sql;
import org.xblackcat.sjpu.storage.ann.ToObjectConverter;

import java.util.List;

/**
 * @author xBlackCat
 */

public interface IFavoriteAH extends IAH {
    /**
     * Returns all the created favorites.
     *
     * @return array of favorites object.
     * @throws StorageException
     */
    @ToObjectConverter(ToFavoriteConverter.class)
    @Sql("SELECT\n" +
                 "  id, type, config\n" +
                 "FROM favorite")
    List<Favorite> getFavorites() throws StorageException;

    /**
     * Store a new favorite in database and return it.
     *
     * @param type   favorite type
     * @param itemId
     * @return
     * @throws StorageException
     */
    @Sql("INSERT INTO favorite (type, config) VALUES (?, ?)")
    int createFavorite(FavoriteType type, int itemId) throws StorageException;

    /**
     * Removes a favorite.
     *
     * @param id favorite id to remove.
     * @throws StorageException
     */
    @Sql("DELETE FROM favorite WHERE id=?")
    void removeFavorite(int id) throws StorageException;

    /**
     * Loads a specified favorite by its id
     *
     * @param favoriteId
     * @return favorite object.
     * @throws StorageException
     */
    @ToObjectConverter(ToFavoriteConverter.class)
    @Sql("SELECT id, type, config FROM favorite WHERE id=?")
    Favorite getFavorite(int favoriteId) throws StorageException;
}
