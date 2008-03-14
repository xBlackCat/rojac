package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.service.data.Moderate;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

class ToModerateConverter implements IToObjectConverter<Moderate> {
    public Moderate convert(ResultSet rs) throws SQLException {
        int messageId = rs.getInt(1);
        int userId = rs.getInt(2);
        int forumId = rs.getInt(3);
        long creationTime = rs.getLong(4);
        return new Moderate(messageId, userId, forumId, creationTime);
    }
}
