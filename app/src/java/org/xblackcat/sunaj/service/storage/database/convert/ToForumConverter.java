package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.service.data.Forum;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

class ToForumConverter implements IToObjectConverter<Forum> {
    public Forum convert(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        int forumGroupId = rs.getInt(2);
        int rated = rs.getInt(3);
        int inTop = rs.getInt(4);
        int rateLimit = rs.getInt(5);
        boolean subscribed = rs.getBoolean(6);
        String shortName = rs.getString(7);
        String name = rs.getString(8);
        return new Forum(id, forumGroupId, inTop, rated, rateLimit, shortName, name, subscribed);
    }
}
