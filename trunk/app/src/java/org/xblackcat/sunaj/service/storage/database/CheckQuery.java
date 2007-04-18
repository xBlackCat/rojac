package org.xblackcat.sunaj.service.storage.database;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public enum CheckQuery implements IPropertiable {
    CHECK_TABLE_FORUM_GROUP,
    CHECK_TABLE_FORUM,
    CHECK_TABLE_NEW_RATING,
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
