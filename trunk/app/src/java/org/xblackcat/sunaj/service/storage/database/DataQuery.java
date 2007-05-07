package org.xblackcat.sunaj.service.storage.database;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public enum DataQuery implements IPropertiable {
    // All object getting queries
    /**
     * The query should fetch the version object properties in following order:
     * <p/>
     * type(int), version_data(byte[64])
     */
    GET_OBJECTS_VERSION,
    /**
     * The query should fetch the forum group object properties in following order:
     * <p/>
     * id(int), name(String), sort_order(int)
     */
    GET_OBJECTS_FORUM_GROUP,
    /**
     * The query should fetch the forum object properties in following order:
     * <p/>
     * id(int), forum group(int), rated(int), inTop(int), rateLimit(int), subscribed(boolean), short name(String), name(String)
     */
    GET_OBJECTS_FORUM,
    /**
     * The query should fetch the new rating object properties in following order:
     * <p/>
     * message id(int), rate(int)
     */
    GET_OBJECTS_NEW_RATING,
    /**
     * The query should fetch the message object properties in following order:
     * <p/>
     * id (int), topic id (int), parent id (int), user id (int), forum id (int), article id (int), user title color (int), user role (int), notify on response (boolean), read (boolean), favorite (int), message date (long), update date (long), moderated date (long), subject (String), message name (String), user nick (String), user title (String), message (String)
     */
    GET_OBJECTS_MESSAGE,
    /**
     * The query should fetch the moderate object properties in following order:
     * <p/>
     * message id(int), user id(int), forum id(int), creation time(long)
     */
    GET_OBJECTS_MODERATE,
    /**
     * The query should fetch the new message object properties in following order:
     * <p/>
     * id (int), parent id(int), forum id(int), subject(String), message(String)
     */
    GET_OBJECTS_NEW_MESSAGE,
    /**
     * The query should fetch the user object properties in following order:
     * <p/>
     * id (int), name (String), nick (String), real_name (String), email (String), home_page (String), specialization (String), where_from (String), origin (String)
     */
    GET_OBJECTS_USER,
    /**
     * The query should fetch the rating object properties in following order:
     * <p/>
     * message id(int), topic id(int), user id(int), user rating(int), rate(int), rate date(long)
     */
    GET_OBJECTS_RATING
    ;

    private final String properyName = this.name().toLowerCase().replace('_', '.');

    public String getPropertyName() {
        return properyName;
    }

    public String toString() {
        return name() + '[' + properyName + ']';
    }
}
