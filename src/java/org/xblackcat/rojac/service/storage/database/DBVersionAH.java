package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.VersionInfo;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.service.storage.IVersionAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
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
                    v.getType().getId());
        } else {
            helper.update(DataQuery.STORE_OBJECT_VERSION,
                    v.getType().getId(),
                    v.getVersion().getBytes());
        }
    }

    public VersionInfo getVersionInfo(VersionType type) throws StorageException {
        return helper.executeSingle(Converters.TO_VERSION_INFO, DataQuery.GET_OBJECT_VERSION, type.getId());
    }
}
