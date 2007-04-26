package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.service.data.NewRating;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

public class ToNewRatingConvertor implements IToObjectConvertor<NewRating> {
    public NewRating convert(ResultSet rs) throws SQLException {
        int messageId = rs.getInt(1);
        int rate = rs.getInt(2);
        return new NewRating(messageId, rate);
    }
}
