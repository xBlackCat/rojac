package org.xblackcat.rojac.service;

import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionInfo;
import org.xblackcat.rojac.data.VersionType;
import org.xblackcat.rojac.service.options.Password;
import org.xblackcat.rojac.service.storage.IVersionAH;
import org.xblackcat.rojac.service.storage.StorageException;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_NAME;
import static org.xblackcat.rojac.service.options.Property.RSDN_USER_PASSWORD;

/**
 * Helper class to access application user information.
 *
 * @author xBlackCat
 */

public final class RojacHelper {
    private RojacHelper() {
    }

    public static String getUserPassword() {
        Password password = RSDN_USER_PASSWORD.get();
        return password == null ? null : password.toString();
    }

    public static boolean isUserRegistered() {
        return RSDN_USER_NAME.isSet() && RSDN_USER_PASSWORD.isSet();

    }

    public static void setUserPassword(char[] p) {
        RSDN_USER_PASSWORD.set(new Password(p));
    }

    public static Version getVersion(VersionType type) throws StorageException {
        IVersionAH vAH = ServiceFactory.getInstance().getStorage().getVersionAH();
        VersionInfo versionInfo = vAH.getVersionInfo(type);
        return versionInfo == null ? new Version() : versionInfo.getVersion();
    }

    public static void setVersion(VersionType type, Version v) throws StorageException {
        IVersionAH vAH = ServiceFactory.getInstance().getStorage().getVersionAH();
        vAH.updateVersionInfo(new VersionInfo(v, type));
    }
}
