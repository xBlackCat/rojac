package org.xblackcat.rojac.service;

import org.xblackcat.rojac.service.options.Password;
import org.xblackcat.rojac.service.options.Property;

/**
 * Helper class to access application user information.
 *
 * @author xBlackCat
 */

public final class RojacHelper {
    private RojacHelper() {
    }

    public static String getUserPassword() {
        Password password = Property.RSDN_USER_PASSWORD.get();
        return password == null ? null : password.toString();
    }

    public static boolean isUserRegistered() {
        return Property.RSDN_USER_NAME.isSet() && Property.RSDN_USER_PASSWORD.isSet();

    }

    public static void setUserPassword(char[] p) {
        Property.RSDN_USER_PASSWORD.set(new Password(p));
    }
}
