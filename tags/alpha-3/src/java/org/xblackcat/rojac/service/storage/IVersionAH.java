package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.VersionInfo;
import org.xblackcat.rojac.data.VersionType;

/**
 * @author ASUS
 */

public interface IVersionAH extends AH {
    void updateVersionInfo(VersionInfo v) throws StorageException;

    VersionInfo getVersionInfo(VersionType type) throws StorageException;
}
