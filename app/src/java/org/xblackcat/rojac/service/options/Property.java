package org.xblackcat.rojac.service.options;

import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Class for holding application properties names.
 * <p/>
 *
 * @author Alexey
 */

public final class Property<T> {
    static IOptionsService OPTIONS_SERVICE;

    /**
     * Complete map of properties names to its objects.
     */
    private static final Collection<Property<?>> ALL_PROPERTIES = new LinkedList<Property<?>>();

    // Global development properties
    public static final Property<Boolean> ROJAC_DEBUG_MODE = create("rojac.global.debug.mode", Boolean.FALSE);

    // Application component properties
    public static final Property<Dimension> ROJAC_MAIN_FRAME_SIZE = create("rojac.main_frame.size", Dimension.class);
    public static final Property<Point> ROJAC_MAIN_FRAME_POSITION = create("rojac.main_frame.position", Point.class);
    public static final Property<Integer> ROJAC_MAIN_FRAME_STATE = create("rojac.main_frame.state", Integer.class);

    // Main GUI properties
    public static final Property<LookAndFeel> ROJAC_GUI_LOOK_AND_FEEL = create("rojac.gui.laf", LookAndFeel.class, UIUtils.getDefaultLAFClass(), new LAFValueChecker());
    public static final Property<Locale> ROJAC_GUI_LOCALE = create("rojac.gui.locale", Locale.getDefault());
    public static final Property<String> ROJAC_GUI_ICONPACK = create("rojac.gui.iconpack", String.class);

    // User properties (login dialog)
    public static final Property<String> RSDN_USER_NAME = create("rojac.rsdn.user.name", String.class);
    public static final Property<Password> RSDN_USER_PASSWORD = create("rojac.rsdn.user.password", Password.class);
    public static final Property<Integer> RSDN_USER_ID = create("rojac.rsdn.user.id", Integer.class);
    public static final Property<Boolean> RSDN_USER_PASSWORD_SAVE = create("rojac.rsdn.user.password.save", Boolean.FALSE);

    //

    // Janus synchronizator properties

    /**
     * This boolean property indicated is should be used GZip compression while retrieving information from Janus WS.
     */
    public static final Property<Boolean> SYNCHRONIZER_USE_GZIP = create("rojac.synchronizer.use_compression", Boolean.FALSE);

    public static final Property<Boolean> SYNCHRONIZER_LOAD_BROKEN_TOPICS_AT_ONCE = create("rojac.synchronizer.load_boken_topics_at_once", Boolean.FALSE);
    public static final Property<Boolean> SYNCHRONIZER_LOAD_USERS = create("rojac.synchronizer.load.users", Boolean.FALSE);
    public static final Property<Integer> SYNCHRONIZER_LOAD_USERS_PORTION = create("rojac.synchronizer.load.users.portion", Integer.valueOf(1000));
    public static final Property<Integer> SYNCHRONIZER_LOAD_MESSAGES_PORTION = create("rojac.synchronizer.load.messages.portion", Integer.valueOf(100));
    public static final Property<Boolean> MESSAGE_PANE_SHOW_MARKS = create("rojac.viewer.show_marks_pane", Boolean.FALSE);

    static <V> Property<V> create(String name, Class<V> type, IValueChecker<V> checker) {
        return create(name, type, null, checker);
    }

    static <V> Property<V> create(String name, V defaultValue, IValueChecker<V> checker) {
        return create(name, (Class<V>) defaultValue.getClass(), defaultValue, checker);
    }

    static <V> Property<V> create(String name, V defaultValue) {
        return create(name, (Class<V>) defaultValue.getClass(), defaultValue, null);
    }

    static <V> Property<V> create(String name, Class<V> type, V defaultValue, IValueChecker<V> checker) {
        Property<V> prop = new Property<V>(name, type, defaultValue, checker);
        ALL_PROPERTIES.add(prop);
        return prop;
    }

    /**
     * Util method for create property object.
     *
     * @param name property name
     * @param type class representing property value type.
     * @param <E>  property value type
     *
     * @return newly generated property object.
     */
    static <E> Property<E> create(String name, Class<E> type) {
        return create(name, type, null, null);
    }

    /**
     * Returns all properties as an array.
     *
     * @return all option properties.
     */
    public static Property<?>[] getAllProperties() {
        return ALL_PROPERTIES.toArray(new Property[ALL_PROPERTIES.size()]);
    }

    private final String name;
    private final Class<T> type;
    private final T defaultValue;
    private final IValueChecker<T> checker;

    private Property(String name, Class<T> type, T defaultValue, IValueChecker<T> checker) {
        if (name == null) throw new NullPointerException("Property name can not be null.");
        if (type == null) throw new NullPointerException("Class type can not be null.");

        this.name = name;
        this.type = type;
        this.checker = checker;
        this.defaultValue = defaultValue;
    }

    /**
     * Returns internal property name.
     *
     * @return internal name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a class of the property values.
     *
     * @return class of the property value.
     */
    public Class<T> getType() {
        return type;
    }

    /**
     * Returns the property checker if any.
     *
     * @return value checker if the one exists and <code>null</code> elsevise.
     */
    public IValueChecker<T> getChecker() {
        return checker;
    }

    /**
     * Checks if the property is set.
     *
     * @return <code>true</code> if property is initiated and <code>false</code> if default value will be used.
     */
    public boolean isSet() {
        return OPTIONS_SERVICE.getProperty(this) != null;
    }

    /**
     * Returns a current value of property or default if it not set.
     *
     * @return current value of property or default one.
     */
    public T get() {
        return get(getDefault());
    }

    /**
     * Returns a current value of the property or specified value if the property is not set.
     *
     * @param defValue value to be treated as default.
     *
     * @return value of the property.
     */
    public T get(T defValue) {
        T val = OPTIONS_SERVICE.getProperty(this);

        return val == null ? defValue : val;
    }

    /**
     * Returns a default value of the property.
     *
     * @return default value of the property.
     */
    public T getDefault() {
        return defaultValue;
    }

    /**
     * Sets a new value to the property.
     *
     * @param val a new value.
     *
     * @return previous value of the property or <code>null</code> if property was empty.
     */
    public T set(T val) {
        return OPTIONS_SERVICE.setProperty(this, val);
    }

    /**
     * Clears the property value and returns previous value.
     *
     * @return previous value of the property before clearing.
     */
    public T clear() {
        return OPTIONS_SERVICE.setProperty(this, null);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Property property = (Property) o;

        return type.equals(property.type) && name.equals(property.name);
    }

    public int hashCode() {
        return 31 * name.hashCode() + type.hashCode();
    }

    public String toString() {
        return "Property[" + name + '(' + type.getName() + ")]";
    }

}
