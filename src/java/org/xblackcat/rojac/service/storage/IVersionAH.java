package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionInfo;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.Sql;

/**
 * @author ASUS
 */

public interface IVersionAH extends IAH {
    @Sql("MERGE INTO version (type, version) VALUES (?, ?)")
    void updateVersionInfo(VersionType type, Version version) throws StorageException;

    @Sql("SELECT type, version FROM version WHERE type=?")
    VersionInfo getVersionInfo(VersionType type) throws StorageException;
}
