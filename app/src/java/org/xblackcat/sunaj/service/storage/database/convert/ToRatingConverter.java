package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.data.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

class ToRatingConverter implements IToObjectConverter<Rating> {
    public Rating convert(ResultSet rs) throws SQLException {
        int messageId = rs.getInt(1);
        int topicId = rs.getInt(2);
        int userId = rs.getInt(3);
        int userRating = rs.getInt(4);
        int rate = rs.getInt(5);
        long rateDate = rs.getLong(6);
        return new Rating(messageId, topicId, userId, userRating, rate, rateDate);
    }
}
