package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.favorite.FavoriteType;
import org.xblackcat.rojac.data.favorite.IFavorite;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */

class ToFavoriteConverter implements IToObjectConverter<IFavorite> {
    @Override
    public IFavorite convert(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String type = rs.getString(2);
        String config = rs.getString(3);
        
        return FavoriteType.restoreFavorite(id, type, config);
    }
}
