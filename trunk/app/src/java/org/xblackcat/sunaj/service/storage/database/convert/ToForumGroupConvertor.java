package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.service.data.ForumGroup;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

public class ToForumGroupConvertor implements IToObjectConvertor<ForumGroup> {
    public ForumGroup convert(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        int sortOrder = rs.getInt(3);
        String name = rs.getString(2);
        return new ForumGroup(id, name, sortOrder);
    }
}
