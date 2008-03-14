package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.service.data.Message;
import org.xblackcat.sunaj.service.data.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

class ToMessageConverter implements IToObjectConverter<Message> {
    public Message convert(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        int topicId = rs.getInt(2);
        int parentId = rs.getInt(3);
        int userId = rs.getInt(4);
        int forumId = rs.getInt(5);
        int articleId = rs.getInt(6);
        int userTitleColor = rs.getInt(7);
        int userRole = rs.getInt(8);
        boolean notifyOnResponse = rs.getBoolean(9);
        boolean read = rs.getBoolean(10);
        Object o = rs.getObject(11);
        Integer favorite;
        if (o instanceof Integer) {
            favorite = (Integer) o;
        } else {
            favorite = null;
        }
        long messageDate = rs.getLong(12);
        long updateDate = rs.getLong(13);
        long moderateDate = rs.getLong(14);
        String subject = rs.getString(15);
        String messageName = rs.getString(16);
        String userNick = rs.getString(17);
        String userTitle = rs.getString(18);
        String message = rs.getString(19);
        return new Message(articleId, forumId, moderateDate, message, messageDate, id, messageName, parentId, subject,
                topicId, updateDate, userId, userNick, Role.values()[userRole], userTitle, userTitleColor,
                notifyOnResponse, read, favorite);
    }
}
