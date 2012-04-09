package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.service.storage.IFavoriteAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
 * @author xBlackCat
 */

final class DBFavoriteAH extends AnAH implements IFavoriteAH {
    DBFavoriteAH(IQueryHolder helper) {
        super(helper);
    }

    @Override
    public Favorite createFavorite(FavoriteType type, int itemId) throws StorageException {
        int nextId = helper.executeSingle(
                Converters.TO_NUMBER,
                DataQuery.GET_NEXT_ID_FAVORITE
        ).intValue();
        helper.update(
                DataQuery.STORE_OBJECT_FAVORITE,
                nextId,
                type.name(),
                String.valueOf(itemId)
        );

        return new Favorite(nextId, itemId, type);
    }

    @Override
    public org.xblackcat.rojac.service.storage.IResult<Favorite> getFavorites() throws StorageException {
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
    public Favorite getFavorite(int favoriteId) throws StorageException {
        return helper.executeSingle(Converters.TO_FAVORITE, DataQuery.GET_OBJECT_FAVORITE, favoriteId);
    }
}
