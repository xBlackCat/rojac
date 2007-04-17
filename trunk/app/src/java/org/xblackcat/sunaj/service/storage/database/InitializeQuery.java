package org.xblackcat.sunaj.service.storage.database;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public enum InitializeQuery implements IPropertiable {
    CREATE_FORUM_GROUP_TABLE,
    CREATE_FORUM_TABLE;

    private final String properyName = this.name().toLowerCase().replace('_', '.');

    public String getPropertyName() {
        return properyName;
    }
}
