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
     * id(int), message id(int), rate(int)
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
    GET_OBJECTS_RATING,

    // All insert object queries
    /**
     * The query for storing the version object. Set the object parameters in following order:
     * <p/>
     * type(int), version_data(byte[64])
     */
    STORE_OBJECTS_VERSION,
    /**
     * The query for storing the forum group object. Set the object parameters in following order:
     * <p/>
     * id(int), name(String), sort_order(int)
     */
    STORE_OBJECTS_FORUM_GROUP,
    /**
     * The query for storing the forum object. Set the object parameters in following order:
     * <p/>
     * id(int), forum group(int), rated(int), inTop(int), rateLimit(int), subscribed(boolean), short name(String), name(String)
     */
    STORE_OBJECTS_FORUM,
    /**
     * The query for storing the new rating object. Set the object parameters in following order:
     * <p/>
     * id(int), message id(int), rate(int)
     */
    STORE_OBJECTS_NEW_RATING,
    /**
     * The query for storing the message object. Set the object parameters in following order:
     * <p/>
     * id (int), topic id (int), parent id (int), user id (int), forum id (int), article id (int), user title color (int), user role (int), notify on response (boolean), read (boolean), favorite (int), message date (long), update date (long), moderated date (long), subject (String), message name (String), user nick (String), user title (String), message (String)
     */
    STORE_OBJECTS_MESSAGE,
    /**
     * The query for storing the moderate object. Set the object parameters in following order:
     * <p/>
     * message id(int), user id(int), forum id(int), creation time(long)
     */
    STORE_OBJECTS_MODERATE,
    /**
     * The query for storing the new message object. Set the object parameters in following order:
     * <p/>
     * id (int), parent id(int), forum id(int), subject(String), message(String)
     */
    STORE_OBJECTS_NEW_MESSAGE,
    /**
     * The query for storing the user object. Set the object parameters in following order:
     * <p/>
     * id (int), name (String), nick (String), real_name (String), email (String), home_page (String), specialization (String), where_from (String), origin (String)
     */
    STORE_OBJECTS_USER,
    /**
     * The query for storing the rating object. Set the object parameters in following order:
     * <p/>
     * message id(int), topic id(int), user id(int), user rating(int), rate(int), rate date(long)
     */
    STORE_OBJECTS_RATING,

    // Object removing queries.
    /**
     * The query for removing the forum object by its id
     */
    REMOVE_OBJECT_FORUM,

    // Queries for getting all the ids of the objects
    GET_IDS_FORUM,

    // Queries for getting object by its id
    GET_OBJECT_VERSION,
    GET_OBJECT_FORUM,
    GET_OBJECT_FORUM_GROUP,
    GET_OBJECT_MESSAGE,
    GET_OBJECT_NEW_MESSAGE,
    GET_OBJECT_USER,

    // Other queries
    /**
     * The query for retrieving ids for the specified forum group. SQL parameters:
     * <p/>
     * forum_group_id(int)
     */
    GET_IDS_FORUM_BY_FORUM_GROUP,
    ;

    private final String properyName = this.name().toLowerCase().replace('_', '.');

    public String getPropertyName() {
        return properyName;
    }

    public String toString() {
        return super.toString() + '[' + properyName + ']';
    }
}
