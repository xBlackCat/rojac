package org.xblackcat.rojac.service;

import org.xblackcat.rojac.service.options.Password;

import static org.xblackcat.rojac.service.options.Property.RSDN_USER_NAME;
import static org.xblackcat.rojac.service.options.Property.RSDN_USER_PASSWORD;

/**
 * Helper class to access application user information.
 *
 * @author xBlackCat
 */

public final class UserHelper {
    private UserHelper() {
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

}
