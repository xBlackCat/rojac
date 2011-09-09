package org.xblackcat.rojac.service.janus.commands;

import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionInfo;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.service.storage.IVersionAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * @author xBlackCat
 */

public class DataHelper {
    public static Version getVersion(VersionType type) throws StorageException {
        IVersionAH vAH = Storage.get(IVersionAH.class);
        VersionInfo versionInfo = vAH.getVersionInfo(type);
        return versionInfo == null ? new Version() : versionInfo.getVersion();
    }

    public static void setVersion(VersionType type, Version v) throws StorageException {
        IVersionAH vAH = Storage.get(IVersionAH.class);
        vAH.updateVersionInfo(new VersionInfo(v, type));
    }
}
