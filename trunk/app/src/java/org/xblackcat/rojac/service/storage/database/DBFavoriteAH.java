package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.favorite.FavoriteType;
import org.xblackcat.rojac.data.favorite.IFavorite;
import org.xblackcat.rojac.service.storage.IFavoriteAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
 * @author xBlackCat
 */

class DBFavoriteAH implements IFavoriteAH{
    private final IQueryExecutor helper;

    public DBFavoriteAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    @Override
    public IFavorite createFavorite(String name, FavoriteType type, String config) throws StorageException {
        int nextId = helper.executeSingle(
                Converters.TO_NUMBER,
                DataQuery.GET_NEXT_ID_FAVORITE
        ).intValue();
        helper.update(
                DataQuery.STORE_OBJECT_FAVORITE,
                nextId,
                name,
                type.name(),
                config
        );
        return FavoriteType.restoreFavorite(nextId, name, type.name(), config);
    }

    @Override
    public IFavorite[] getFavorites() throws StorageException {
        return new IFavorite[0];
    }

    @Override
    public void removeFavorite(int id) throws StorageException {
    }
}
