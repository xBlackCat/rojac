package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.NewRating;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ASUS
 */

class ToNewRatingConverter implements IToObjectConverter<NewRating> {
    public NewRating convert(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        int messageId = rs.getInt(2);
        int rate = rs.getInt(3);
        return new NewRating(id, messageId, rate);
    }
}
