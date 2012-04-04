package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.*;
import ru.rsdn.Janus.RequestForumInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */

public final class Converters {
    public static final IToObjectConverter<Forum> TO_FORUM = new ToForumConverter();
    public static final IToObjectConverter<ForumGroup> TO_FORUM_GROUP = new ToForumGroupConverter();
    public static final IToObjectConverter<Moderate> TO_MODERATE = new ToModerateConverter();
    public static final IToObjectConverter<NewMessage> TO_NEW_MESSAGE = new ToNewMessageConverter();
    public static final IToObjectConverter<NewModerate> TO_NEW_MODERATE = new ToNewModerateConverter();
    public static final IToObjectConverter<NewRating> TO_NEW_RATING = new ToNewRatingConverter();
    public static final IToObjectConverter<Rating> TO_RATING = new ToRatingConverter();
    public static final IToObjectConverter<User> TO_USER = new ToUserConverter();
    public static final IToObjectConverter<VersionInfo> TO_VERSION_INFO = new ToVersionInfoConverter();
    public static final IToObjectConverter<String> TO_STRING = new ToScalarConverter<>();
    public static final IToObjectConverter<Number> TO_NUMBER = new ToScalarConverter<>();
    public static final IToObjectConverter<Integer> TO_INTEGER = new ToScalarConverter<>();
    public static final IToObjectConverter<Long> TO_LONG = new ToScalarConverter<>();
    public static final IToObjectConverter<Boolean> TO_BOOLEAN = new ToBooleanConverter();
    public static final IToObjectConverter<Mark> TO_MARK = new ToMarkConverter();
    public static final IToObjectConverter<MarkStat> TO_MARK_STAT = new ToMarkStatConverter();
    public static final IToObjectConverter<MessageData> TO_MESSAGE_DATA = new ToMessageDataConverter();
    public static final IToObjectConverter<ItemStatisticData<MessageData>> TO_THREAD_DATA = new ToThreadDataConverter();
    public static final IToObjectConverter<ItemStatisticData<Forum>> TO_FORUM_DATA = new ToForumDataConverter();
    public static final IToObjectConverter<Favorite> TO_FAVORITE = new ToFavoriteConverter();
    public static final IToObjectConverter<AffectedMessage> TO_AFFECTED_MESSAGE_CONVERTER = new ToAffectedMessageConverter();
    public static final IToObjectConverter<DiscussionStatistic> TO_DISCUSSION_STATISTIC = new IToObjectConverter<DiscussionStatistic>() {
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
    };

    public static final IToObjectConverter<RequestForumInfo> TO_SUBSCRIBED_FORUM = new IToObjectConverter<RequestForumInfo>() {
        @Override
        public RequestForumInfo convert(ResultSet rs) throws SQLException {
            int forumId = rs.getInt(1);
            boolean empty = rs.getBoolean(2);

            return new RequestForumInfo(forumId, empty);
        }
    };

    private Converters() {
    }
}
