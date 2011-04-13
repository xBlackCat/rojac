package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.AffectedMessage;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */

class ToAffectedMessageConverter implements IToObjectConverter<AffectedMessage> {
    @Override
    public AffectedMessage convert(ResultSet rs) throws SQLException {
        int messageId = rs.getInt(1);
        int topicId = rs.getInt(2);
        int forumId = rs.getInt(3);

        if (topicId == 0) {
            // The message is a topic root
            topicId = messageId;
        }

        return new AffectedMessage(messageId, topicId, forumId);
    }
}
