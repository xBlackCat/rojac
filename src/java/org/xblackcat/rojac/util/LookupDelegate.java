package org.xblackcat.rojac.util;

import org.xblackcat.rojac.RojacDebugException;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author xBlackCat Date: 22.06.11
 */
public final class LookupDelegate {
    private static final Class<?> DEFAULT_LOOKUP;
    private static final Method METHOD_GET;
    private static final Method METHOD_GET_BOOLEAN;
    private static final Method METHOD_GET_BORDER;
    private static final Method METHOD_GET_COLOR;
    private static final Method METHOD_GET_ICON;
    private static final Method METHOD_GET_ICON_;
    private static final Method METHOD_GET_INSETS;
    private static final Method METHOD_GET_INT;
    private static final Method METHOD_GET_INT_;

    static {
        try {
            DEFAULT_LOOKUP = Class.forName("sun.swing.DefaultLookup");
        } catch (ClassNotFoundException e) {
            throw new RojacDebugException("Can not initialize sun.swing.DefaultLookup class.", e);
        }

        try {
            METHOD_GET = DEFAULT_LOOKUP.getMethod("get", JComponent.class, ComponentUI.class, String.class);
            METHOD_GET_BOOLEAN = DEFAULT_LOOKUP.getMethod("getBoolean", JComponent.class, ComponentUI.class, String.class, Boolean.TYPE);
            METHOD_GET_BORDER = DEFAULT_LOOKUP.getMethod("getBorder", JComponent.class, ComponentUI.class, String.class);
            METHOD_GET_COLOR = DEFAULT_LOOKUP.getMethod("getColor", JComponent.class, ComponentUI.class, String.class);
            METHOD_GET_ICON = DEFAULT_LOOKUP.getMethod("getIcon", JComponent.class, ComponentUI.class, String.class);
            METHOD_GET_ICON_ = DEFAULT_LOOKUP.getMethod("getIcon", JComponent.class, ComponentUI.class, String.class, Icon.class);
            METHOD_GET_INSETS = DEFAULT_LOOKUP.getMethod("getInsets", JComponent.class, ComponentUI.class, String.class);
            METHOD_GET_INT = DEFAULT_LOOKUP.getMethod("getInt", JComponent.class, ComponentUI.class, String.class);
            METHOD_GET_INT_ = DEFAULT_LOOKUP.getMethod("getInt", JComponent.class, ComponentUI.class, String.class, Integer.TYPE);
        } catch (NoSuchMethodException e) {
            throw new RojacDebugException("Can not initialize methods", e);
        }
    }

    private LookupDelegate() {
    }

    public static Object get(JComponent jComponent, ComponentUI componentUI, String s) {
        return invokeMethod(METHOD_GET, jComponent, componentUI, s);
    }

    public static boolean getBoolean(JComponent jComponent, ComponentUI componentUI, String s, boolean b) {
        Boolean bool = invokeMethod(METHOD_GET_BOOLEAN, jComponent, componentUI, s, b);
        return bool;
    }

    public static Border getBorder(JComponent jComponent, ComponentUI componentUI, String s) {
        return invokeMethod(METHOD_GET_BORDER, jComponent, componentUI, s);
    }

    public static Color getColor(JComponent jComponent, ComponentUI componentUI, String s) {
        return invokeMethod(METHOD_GET_COLOR, jComponent, componentUI, s);
    }

    public static Icon getIcon(JComponent jComponent, ComponentUI componentUI, String s) {
        return invokeMethod(METHOD_GET_ICON, jComponent, componentUI, s);
    }

    public static Icon getIcon(JComponent jComponent, ComponentUI componentUI, String s, Icon icon) {
        return invokeMethod(METHOD_GET_ICON_, jComponent, componentUI, s, icon);
    }

    public static Insets getInsets(JComponent jComponent, ComponentUI componentUI, String s) {
        return invokeMethod(METHOD_GET_INSETS, jComponent, componentUI, s);
    }

    public static int getInt(JComponent jComponent, ComponentUI componentUI, String s) {
        Integer i = invokeMethod(METHOD_GET_INT, jComponent, componentUI, s);
        return i;
    }

    public static int getInt(JComponent jComponent, ComponentUI componentUI, String s, int i) {
        Integer integer = invokeMethod(METHOD_GET_INT_, jComponent, componentUI, s, i);
        return integer;
    }

    @SuppressWarnings({"unchecked"})
    private static <T> T invokeMethod(Method method, Object... params) {
        try {
            return (T) method.invoke(null, params);
        } catch (IllegalAccessException e) {
            throw new RojacDebugException("Can not invoke a method " + method, e);
        } catch (InvocationTargetException e) {
            throw new RojacDebugException("Method " + method + " throws an exception", e);
        }
    }
}
