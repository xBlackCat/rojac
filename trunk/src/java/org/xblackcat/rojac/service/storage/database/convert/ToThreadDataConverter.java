package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.sjpu.storage.converter.IToObjectConverter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */
public class ToThreadDataConverter implements IToObjectConverter<ItemStatisticData<MessageData>> {
    @Override
    public ItemStatisticData<MessageData> convert(ResultSet rs) throws SQLException {
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
        int parentUserId = rs.getInt(13);
        boolean userIgnored = rs.getBoolean(14);

        MessageData messageData = new MessageData(
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
                topicIgnored,
                parentUserId,
                userIgnored
        );

        int totals = rs.getInt(15);
        int unreadPosts = rs.getInt(16);
        int unreadReplies = rs.getInt(17);
        Long lastPostDate = rs.getLong(18);
        if (rs.wasNull()) {
            lastPostDate = null;
        }

        DiscussionStatistic discussionStatistic = new DiscussionStatistic(totals, unreadPosts, unreadReplies, lastPostDate);

        return new ItemStatisticData<>(messageData, discussionStatistic);
    }
}
