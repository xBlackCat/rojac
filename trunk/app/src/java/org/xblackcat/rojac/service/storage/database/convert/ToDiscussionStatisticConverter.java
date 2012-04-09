package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.DiscussionStatistic;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 09.04.12 11:46
 *
 * @author xBlackCat
 */
class ToDiscussionStatisticConverter implements IToObjectConverter<DiscussionStatistic> {
    @Override
    public DiscussionStatistic convert(ResultSet rs) throws SQLException {
        int totals = rs.getInt(1);
        int unreadPosts = rs.getInt(2);
        int unreadReplies = rs.getInt(3);
        Long lastPostDate = rs.getLong(4);
        if (rs.wasNull()) {
            lastPostDate = null;
        }

        return new DiscussionStatistic(totals, unreadPosts, lastPostDate, unreadReplies);
    }
}
