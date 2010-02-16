package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.utils.ResourceUtils;

/**
 * @author ASUS
 */

public enum InitializeQuery implements IPropertiable {
    // Table creation queries
    CREATE_TABLE_VERSION,
    CREATE_TABLE_FORUM_GROUP,
    CREATE_TABLE_FORUM,
    CREATE_TABLE_NEW_RATING,
    CREATE_TABLE_MESSAGE,
    CREATE_TABLE_MODERATE,
    CREATE_TABLE_NEW_MESSAGE,
    CREATE_TABLE_USER,
    CREATE_TABLE_RATING,
    CREATE_TABLE_EXTRA_MESSAGE;

    private final String properyName = ResourceUtils.constantToProperty(this.name());

    public String getPropertyName() {
        return properyName;
    }

    public String toString() {
        return name() + '[' + properyName + ']';
    }
}
