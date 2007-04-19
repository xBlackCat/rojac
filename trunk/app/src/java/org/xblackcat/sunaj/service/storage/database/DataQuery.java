package org.xblackcat.sunaj.service.storage.database;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public enum DataQuery implements IPropertiable {
    // Object getting queries
    GET_OBJECT_VERSION,
    GET_OBJECT_FORUM_GROUP,
    GET_OBJECT_FORUM,
    GET_OBJECT_NEW_RATING,
    GET_OBJECT_MESSAGE,
    GET_OBJECT_MODERATE,
    GET_OBJECT_NEW_MESSAGE,
    GET_OBJECT_USER,
    GET_OBJECT_RATING
    ;

    private final String properyName = this.name().toLowerCase().replace('_', '.');

    public String getPropertyName() {
        return properyName;
    }

    public String toString() {
        return name() + '[' + properyName + ']';
    }
}
