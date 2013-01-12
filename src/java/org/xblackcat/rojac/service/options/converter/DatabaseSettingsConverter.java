package org.xblackcat.rojac.service.options.converter;

import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.util.DatabaseUtils;

/**
 * 09.09.11 12:05
 *
 * @author xBlackCat
 */
public class DatabaseSettingsConverter implements IConverter<DatabaseSettings> {
    @Override
    public DatabaseSettings convert(String s) {
        return DatabaseUtils.convert(s);
    }

    @Override
    public String toString(DatabaseSettings o) {
        return DatabaseUtils.convert(o);
    }
}
