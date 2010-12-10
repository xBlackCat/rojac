package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.favorite.FavoriteType;
import org.xblackcat.rojac.data.favorite.IFavorite;
import org.xblackcat.rojac.service.storage.IFavoriteAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
 * @author xBlackCat
 */

public class DBFavoriteAH implements IFavoriteAH{
    private final IQueryExecutor helper;

    public DBFavoriteAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    @Override
    public IFavorite createFavorite(String name, FavoriteType type, String config) throws StorageException {
        long nextId = helper.executeSingle(
                Converters.TO_NUMBER,
                DataQuery.GET_NEXT_ID_NEW_MESSAGE
        ).longValue();
        helper.update(
                DataQuery.STORE_OBJECT_NEW_MESSAGE,
                nextId,
                nm.getParentId(),
                nm.getForumId(),
                nm.getSubject(),
                nm.getMessage()
        );
        return null;
    }

    @Override
    public IFavorite[] getFavorites() throws StorageException {
        return new IFavorite[0];
    }

    @Override
    public void removeFavorite(int id) throws StorageException {
    }
}
