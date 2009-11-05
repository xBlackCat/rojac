package org.xblackcat.rojac.service;

import org.xblackcat.rojac.service.options.IOptionsService;
import org.xblackcat.rojac.service.options.Password;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Helper class to access application user information.
 *
 * @author xBlackCat
 */

public final class RojacHelper {
    private static IOptionsService optionsService = ServiceFactory.getInstance().getOptionsService();

    private RojacHelper() {
    }

    public static String getUserName() {
        return optionsService.getProperty(Property.RSDN_USER_NAME);
    }

    public static String getUserPassword() {
        Password password = optionsService.getProperty(Property.RSDN_USER_PASSWORD);
        return password == null ? null : password.toString();
    }

    public static boolean isUserRegistered() {
        return getUserName() != null && getUserPassword() != null;

    }

    public static boolean shouldForgetPassword() {
        return optionsService.getProperty(Property.RSDN_USER_PASSWORD_SAVE) != Boolean.TRUE;
    }

    public static void shouldStorePassword(boolean newState) {
        optionsService.setProperty(Property.RSDN_USER_PASSWORD_SAVE, Boolean.valueOf(newState));
    }

    public static void setUserPassword(char[] p) {
        optionsService.setProperty(Property.RSDN_USER_PASSWORD, new Password(p));
    }

    public static void setUserName(String userName) {
        optionsService.setProperty(Property.RSDN_USER_NAME, userName);
    }

    public static int getUserId() {
        return optionsService.getProperty(Property.RSDN_USER_ID);
    }

    public static void setUserId(int id) {
        optionsService.setProperty(Property.RSDN_USER_ID, id);
    }

    public static String getThemePath(String path) {
        return "/images/" + path;
    }

    public static Icon loadIcon(String path) {
        return ResourceUtils.loadIcon(getThemePath(path));
    }

    public static Image loadImage(String path) {
        return ResourceUtils.loadImage(getThemePath(path));
    }
}
