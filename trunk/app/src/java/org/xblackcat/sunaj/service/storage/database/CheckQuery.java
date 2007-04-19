package org.xblackcat.sunaj.service.storage.database;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public enum CheckQuery implements IPropertiable {
    // Table checking queries
    CHECK_TABLE_VERSION,
    CHECK_TABLE_FORUM_GROUP,
    CHECK_TABLE_FORUM,
    CHECK_TABLE_NEW_RATING,
    CHECK_TABLE_MESSAGE,
    CHECK_TABLE_MODERATE,
    CHECK_TABLE_NEW_MESSAGE,
    CHECK_TABLE_USER,
    CHECK_TABLE_RATING
    ;

    private final String properyName = this.name().toLowerCase().replace('_', '.');

    public String getPropertyName() {
        return properyName;
    }

    public String toString() {
        return name() + '[' + properyName + ']';
    }
}
