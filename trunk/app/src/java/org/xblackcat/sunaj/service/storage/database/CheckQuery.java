package org.xblackcat.sunaj.service.storage.database;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public enum CheckQuery implements IPropertiable {
    CHECK_FORUM_GROUP_TABLE,
    CHECK_FORUM_TABLE;

    private final String properyName = this.name().toLowerCase().replace('_', '.');

    public String getPropertyName() {
        return properyName;
    }
}
