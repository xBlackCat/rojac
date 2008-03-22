package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.data.*;

/**
 * Date: 28 лют 2008
 *
 * @author xBlackCat
 */

public final class Converters {
    public static final IToObjectConverter<Forum> TO_FORUM_CONVERTER = new ToForumConverter();
    public static final IToObjectConverter<ForumGroup> TO_FORUM_GROUP_CONVERTER = new ToForumGroupConverter();
    public static final IToObjectConverter<Message> TO_MESSAGE_CONVERTER = new ToMessageConverter();
    public static final IToObjectConverter<Moderate> TO_MODERATE_CONVERTER = new ToModerateConverter();
    public static final IToObjectConverter<NewMessage> TO_NEW_MESSAGE_CONVERTER = new ToNewMessageConverter();
    public static final IToObjectConverter<NewRating> TO_NEW_RATING_CONVERTER = new ToNewRatingConverter();
    public static final IToObjectConverter<Rating> TO_RATING_CONVERTER = new ToRatingConverter();
    public static final IToObjectConverter<User> TO_USER_CONVERTER = new ToUserConverter();
    public static final IToObjectConverter<VersionInfo> TO_VERSION_INFO_CONVERTER = new ToVersionInfoConverter();
    public static final IToObjectConverter<String> TO_STRING_CONVERTER = new ToScalarConverter<String>();
    public static final IToObjectConverter<Integer> TO_INTEGER_CONVERTER = new ToScalarConverter<Integer>();
    public static final IToObjectConverter<Boolean> TO_BOOLEAN_CONVERTER = new ToScalarConverter<Boolean>();
    public static final IToObjectConverter<Mark> TO_MARK_CONVERTER = new ToMarkConverter();

    private Converters() {
    }
}
