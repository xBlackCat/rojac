package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.data.NewModerate;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

class ToNewModerateConverter implements IToObjectConverter<NewModerate> {
    public NewModerate convert(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        int messageId = rs.getInt(2);
        int forumId = rs.getInt(3);
        int action = rs.getInt(4);
        String description = rs.getString(5);
        boolean asModerator = rs.getBoolean(6);
        return new NewModerate(id, messageId, action, forumId, description, asModerator);
    }
}