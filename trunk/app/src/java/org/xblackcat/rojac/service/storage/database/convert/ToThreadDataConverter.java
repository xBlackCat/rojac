package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.rojac.data.MessageData;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */
class ToThreadDataConverter implements IToObjectConverter<ItemStatisticData<MessageData>> {
    @Override
    public ItemStatisticData<MessageData> convert(ResultSet rs) throws SQLException {
        MessageData messageData = Converters.TO_MESSAGE_DATA.convert(rs);

        int totals = rs.getInt(15);
        int unreadPosts = rs.getInt(16);
        int unreadReplies = rs.getInt(17);
        Long lastPostDate = rs.getLong(18);
        if (rs.wasNull()) {
            lastPostDate = null;
        }

        DiscussionStatistic discussionStatistic = new DiscussionStatistic(totals, unreadPosts, lastPostDate, unreadReplies);

        return new ItemStatisticData<>(messageData, discussionStatistic);
    }
}
