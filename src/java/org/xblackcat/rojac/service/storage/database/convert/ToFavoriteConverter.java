package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.sjpu.storage.converter.IToObjectConverter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */

public class ToFavoriteConverter implements IToObjectConverter<Favorite> {
    @Override
    public Favorite convert(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String type = rs.getString(2);
        String config = rs.getString(3);

        FavoriteType realType;
        try {
            realType = FavoriteType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new SQLException("Invalid enum name: " + type, e);
        }

        return new Favorite(id, Integer.parseInt(config), realType);
    }
}
