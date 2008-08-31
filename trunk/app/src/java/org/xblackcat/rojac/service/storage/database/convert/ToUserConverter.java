package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

class ToUserConverter implements IToObjectConverter<User> {
    public User convert(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String name = rs.getString(2);
        String nick = rs.getString(3);
        String realName = rs.getString(4);
        String email = rs.getString(5);
        String homePage = rs.getString(6);
        String specialization = rs.getString(7);
        String whereFrom = rs.getString(8);
        String origin = rs.getString(9);
        return new User(id, name, nick, realName, email, homePage, specialization, whereFrom, origin);
    }
}
