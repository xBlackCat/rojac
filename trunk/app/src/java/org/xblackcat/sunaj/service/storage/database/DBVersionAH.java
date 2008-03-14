package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.service.data.VersionInfo;
import org.xblackcat.sunaj.service.data.VersionType;
import org.xblackcat.sunaj.service.storage.IVersionAH;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.Converters;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

final class DBVersionAH implements IVersionAH {
    private final IQueryExecutor helper;

    DBVersionAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void updateVersionInfo(VersionInfo v) throws StorageException {
        if (getVersionInfo(v.getType()) != null) {
            helper.update(DataQuery.UPDATE_OBJECT_VERSION,
                    v.getVersion().getBytes(),
                    v.getType().ordinal());
        } else {
            helper.update(DataQuery.STORE_OBJECT_VERSION,
                    v.getType().ordinal(),
                    v.getVersion().getBytes());
        }
    }

    public VersionInfo getVersionInfo(VersionType type) throws StorageException {
        return helper.executeSingle(Converters.TO_VERSION_INFO_CONVERTER, DataQuery.GET_OBJECT_VERSION, type.ordinal());
    }
}
