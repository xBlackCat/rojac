package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.*;

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
    public static final IToObjectConverter<String> TO_STRING = new ToScalarConverter<String>();
    public static final IToObjectConverter<Number> TO_NUMBER = new ToScalarConverter<Number>();
    public static final IToObjectConverter<Integer> TO_INTEGER = new ToScalarConverter<Integer>();
    public static final IToObjectConverter<Long> TO_LONG = new ToScalarConverter<Long>();
    public static final IToObjectConverter<Boolean> TO_BOOLEAN = new ToBooleanConverter();
    public static final IToObjectConverter<Mark> TO_MARK = new ToMarkConverter();
    public static final IToObjectConverter<MarkStat> TO_MARK_STAT = new ToMarkStatConverter();
    public static final IToObjectConverter<MessageData> TO_MESSAGE_DATA = new ToMessageDataConverter();
    public static final IToObjectConverter<ThreadStatData> TO_THREAD_DATA = new ToThreadDataConverter();

    private Converters() {
    }
}
