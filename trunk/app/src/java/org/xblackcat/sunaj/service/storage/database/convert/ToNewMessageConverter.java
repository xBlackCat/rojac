package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.data.NewMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

class ToNewMessageConverter implements IToObjectConverter<NewMessage> {
    public NewMessage convert(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        int parentId = rs.getInt(2);
        int forumId = rs.getInt(3);
        String subject = rs.getString(4);
        String message = rs.getString(5);
        return new NewMessage(id, parentId, forumId, subject, message);
    }
}
