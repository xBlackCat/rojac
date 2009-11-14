package org.xblackcat.rojac.service;

import org.xblackcat.rojac.service.options.Password;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.util.MissingResourceException;

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

    public static String getThemePath(String path) {
        return "/images/" + path;
    }

    public static Icon loadIcon(String path) throws MissingResourceException {
        return ResourceUtils.loadIcon(getThemePath(path));
    }

    public static Image loadImage(String path) throws MissingResourceException {
        return ResourceUtils.loadImage(getThemePath(path));
    }
}
