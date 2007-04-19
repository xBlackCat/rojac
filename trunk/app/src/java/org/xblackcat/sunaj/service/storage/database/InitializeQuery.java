package org.xblackcat.sunaj.service.storage.database;

/**
 * Date: 17.04.2007
 *
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
    CREATE_TABLE_RATING
    ;

    private final String properyName = this.name().toLowerCase().replace('_', '.');

    public String getPropertyName() {
        return properyName;
    }

    public String toString() {
        return name() + '[' + properyName + ']';
    }
}
