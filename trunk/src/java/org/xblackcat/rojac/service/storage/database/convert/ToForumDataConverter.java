package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ItemStatisticData;
import org.xblackcat.sjpu.storage.converter.IToObjectConverter;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */
public class ToForumDataConverter implements IToObjectConverter<ItemStatisticData<Forum>> {
    @Override
    public ItemStatisticData<Forum> convert(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        int forumGroupId = rs.getInt(2);
        int rated = rs.getInt(3);
        int inTop = rs.getInt(4);
        int rateLimit = rs.getInt(5);
        boolean subscribed = rs.getBoolean(6);
        String shortName = rs.getString(7);
        String name = rs.getString(8);
        Forum forum = new Forum(id, forumGroupId, rated, inTop, rateLimit, subscribed, shortName, name);

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
