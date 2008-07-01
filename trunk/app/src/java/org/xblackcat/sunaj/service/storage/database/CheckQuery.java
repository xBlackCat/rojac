package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.utils.ResourceUtils;

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
    CHECK_TABLE_RATING,
    CHECK_TABLE_EXTRA_MESSAGE;

    private final String properyName = ResourceUtils.constantToProperty(this.name());

    public String getPropertyName() {
        return properyName;
    }

    public String toString() {
        return name() + '[' + properyName + ']';
    }
}
