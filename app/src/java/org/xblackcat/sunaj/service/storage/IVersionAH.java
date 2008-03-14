package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.VersionInfo;
import org.xblackcat.sunaj.service.data.VersionType;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

public interface IVersionAH extends AH {
    void updateVersionInfo(VersionInfo v) throws StorageException;

    VersionInfo getVersionInfo(VersionType type) throws StorageException;
}
