package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.service.storage.IFavoriteAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

import java.util.List;

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
    public List<IFavorite> getFavorites() throws StorageException {
        return helper.execute(
                Converters.TO_FAVORITE,
                DataQuery.GET_OBJECTS_FAVORITE
        );
    }

    @Override
    public void removeFavorite(int id) throws StorageException {
        helper.update(DataQuery.REMOVE_OBJECT_FAVORITE, id);
    }

    @Override
    public IFavorite getFavorite(int favoriteId) throws StorageException {
        return helper.executeSingle(Converters.TO_FAVORITE, DataQuery.GET_OBJECT_FAVORITE, favoriteId);
    }
}
