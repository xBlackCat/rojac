package org.xblackcat.rojac.service.options.converter;

import org.xblackcat.rojac.service.storage.database.DBConfig;
import org.xblackcat.rojac.util.DatabaseUtils;

/**
 * 09.09.11 12:05
 *
 * @author xBlackCat
 */
public class DatabaseSettingsConverter implements IConverter<DBConfig> {
    @Override
    public DBConfig convert(String s) {
        return DatabaseUtils.convert(s);
    }

    @Override
    public String toString(DBConfig o) {
        return DatabaseUtils.convert(o);
    }
}
