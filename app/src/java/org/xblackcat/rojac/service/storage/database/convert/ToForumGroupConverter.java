package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.ForumGroup;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

class ToForumGroupConverter implements IToObjectConverter<ForumGroup> {
    public ForumGroup convert(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        int sortOrder = rs.getInt(3);
        String name = rs.getString(2);
        return new ForumGroup(id, name, sortOrder);
    }
}
