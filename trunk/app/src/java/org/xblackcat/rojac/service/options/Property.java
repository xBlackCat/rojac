package org.xblackcat.rojac.service.options;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Class for holding application properties names.
 * <p/>
 *
 * @author Alexey
 */

public final class Property<T> {
    /**
     * Complete map of properties names to its objects.
     */
    private static final Map<String, Property<?>> ALL_PROPERTIES = new HashMap<String, Property<?>>();

    // Global development properties
    public static final Property<Boolean> ROJAC_DEBUG_MODE = create("rojac.global.debug.mode", Boolean.class);

    // Application component properties
    public static final Property<Dimension> ROJAC_MAIN_FRAME_SIZE = create("rojac.main_frame.size", Dimension.class);
    public static final Property<Point> ROJAC_MAIN_FRAME_POSITION = create("rojac.main_frame.position", Point.class);
    public static final Property<Integer> ROJAC_MAIN_FRAME_STATE = create("rojac.main_frame.state", Integer.class);

    // Main GUI properties
    public static final Property<LookAndFeel> ROJAC_GUI_LOOK_AND_FEEL = create("rojac.gui.laf", LookAndFeel.class);
    public static final Property<Locale> ROJAC_GUI_LOCALE = create("rojac.gui.locale", Locale.class);
    public static final Property<String> ROJAC_GUI_ICONPACK = create("rojac.gui.iconpack", String.class);

    // User properties (login dialog)
    public static final Property<String> RSDN_USER_NAME = create("rsdn.user.name", String.class);
    public static final Property<Password> RSDN_USER_PASSWORD = create("rsdn.user.password", Password.class);
    public static final Property<Integer> RSDN_USER_ID = create("rsdn.user.id", Integer.class);
    public static final Property<Boolean> RSDN_USER_PASSWORD_SAVE = create("rsdn.user.password.store", Boolean.class);

    //

    // Janus synchronizator properties

    /**
     * This boolean property indicated is should be used GZip compression while retrieving information from Janus WS.
     */
    public static final Property<Boolean> SERVICE_JANUS_USE_GZIP = create("rojac.service.janusws.use_gzip", Boolean.class);

    public static final Property<Boolean> SYNCHRONIZER_LOAD_BROKEN_TOPICS_AT_ONCE = create("rojac.synchronizer.load_boken_topics_at_once", Boolean.class);
    public static final Property<Boolean> SYNCHRONIZER_LOAD_USERS = create("rojac.synchronizer.load.users", Boolean.class);
    public static final Property<Integer> SYNCHRONIZER_LOAD_USERS_PORTION = create("rojac.synchronizer.load.users.portion", Integer.class);
    public static final Property<Integer> SYNCHRONIZER_LOAD_MESSAGES_PORTION = create("rojac.synchronizer.load.messages.portion", Integer.class);
    public static final Property<Boolean> MESSAGE_PANE_SHOW_MARKS = create("rojac.viewer.show.marks.pane", Boolean.class);

    static <V> Property<V> create(String name, Class<V> type, IValueChecker<V> checker) {
        Property<V> prop = new Property<V>(name, type, checker);
        ALL_PROPERTIES.put(name, prop);
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
        return create(name, type, null);
    }

    public static Property<?> getPropertyForName(String name) {
        return ALL_PROPERTIES.get(name);
    }

    /**
     * Returns all properties as an array.
     *
     * @return all option properties.
     */
    public static Property<?>[] getAllProperties() {
        Collection<Property<?>> properties = ALL_PROPERTIES.values();
        return properties.toArray(new Property[properties.size()]);
    }

    private final String name;
    private final Class<T> type;
    private final IValueChecker<T> checker;

    private Property(String name, Class<T> type, IValueChecker<T> checker) {
        if (name == null) throw new NullPointerException("Property name can not be null.");
        if (type == null) throw new NullPointerException("Class type can not be null.");

        this.name = name;
        this.type = type;
        this.checker = checker;
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
