package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.MessageData;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */
class ToMessageDataConverter implements IToObjectConverter<MessageData> {
    @Override
    public MessageData convert(ResultSet rs) throws SQLException {
        int messageId = rs.getInt(1);
        int topicId = rs.getInt(2);
        int parentId = rs.getInt(3);
        int forumId = rs.getInt(4);
        int userId = rs.getInt(5);
        String subject = rs.getString(6);
        String userName = rs.getString(7);
        long messageDate = rs.getLong(8);
        long updateDate = rs.getLong(9);
        boolean read = rs.getBoolean(10);
        String rating = rs.getString(11);
        boolean topicIgnored = rs.getBoolean(12);

        return new MessageData(
                messageId,
                topicId,
                parentId,
                forumId,
                userId,
                subject,
                userName,
                messageDate,
                updateDate,
                read,
                rating,
                topicIgnored
        );
    }
}
