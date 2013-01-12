package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ItemStatisticData;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */
class ToForumDataConverter implements IToObjectConverter<ItemStatisticData<Forum>> {
    @Override
    public ItemStatisticData<Forum> convert(ResultSet rs) throws SQLException {
        Forum forum = Converters.TO_FORUM.convert(rs);

        int totals = rs.getInt(9);
//        int unreadPosts = rs.getInt(10);
//        int unreadReplies = rs.getInt(11);
        Long lastPostDate = rs.getLong(12);
        if (rs.wasNull()) {
            lastPostDate = null;
        }

        DiscussionStatistic discussionStatistic = new DiscussionStatistic(totals, 0, 0, lastPostDate);

        return new ItemStatisticData<>(forum, discussionStatistic);
    }
}
