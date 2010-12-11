package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.favorite.FavoriteType;
import org.xblackcat.rojac.data.favorite.IFavorite;
import org.xblackcat.rojac.service.storage.IFavoriteAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

import java.util.Collection;

/**
 * @author xBlackCat
 */

class DBFavoriteAH implements IFavoriteAH {
    private final IQueryExecutor helper;

    public DBFavoriteAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    @Override
    public IFavorite createFavorite(FavoriteType type, String config) throws StorageException {
        int nextId = helper.executeSingle(
                Converters.TO_NUMBER,
                DataQuery.GET_NEXT_ID_FAVORITE
        ).intValue();
        helper.update(
                DataQuery.STORE_OBJECT_FAVORITE,
                nextId,
                type.name(),
                config
        );
        return FavoriteType.restoreFavorite(nextId, type.name(), config);
    }

    @Override
    public IFavorite[] getFavorites() throws StorageException {
        Collection<IFavorite> favorites = helper.execute(
                Converters.TO_FAVORITE,
                DataQuery.GET_OBJECTS_FAVORITE
        );
        return favorites.toArray(new IFavorite[favorites.size()]);
    }

    @Override
    public void removeFavorite(int id) throws StorageException {
        helper.update(DataQuery.REMOVE_OBJECT_FAVORITE, id);
    }
}
