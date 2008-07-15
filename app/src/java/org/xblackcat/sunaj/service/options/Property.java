package org.xblackcat.sunaj.service.options;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for holding application properties names.
 * <p/>
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

public final class Property<T> {
    /**
     * Complete map of properties names to its objects.
     */
    private static final Map<String, Property<?>> ALL_PROPERTIES = new HashMap<String, Property<?>>();

    // Global development properties
    public static final Property<Boolean> SUNAJ_DEBUG_MODE = create("sunaj.global.debug.mode", Boolean.class);

    // Application component properties
    public static final Property<Dimension> SUNAJ_MAIN_FRAME_SIZE = create("sunaj.main_frame.size", Dimension.class);
    public static final Property<Point> SUNAJ_MAIN_FRAME_POSITION = create("sunaj.main_frame.position", Point.class);

    // GUI properties
    public static final Property<LookAndFeel> SUNAJ_GUI_LOOK_AND_FEEL = create("sunaj.gui.laf", LookAndFeel.class);

    // User properties (login dialog)
    public static final Property<String> RSDN_USER_NAME = create("rsdn.user.name", String.class);
    public static final Property<Password> RSDN_USER_PASSWORD = create("rsdn.user.password", Password.class);
    public static final Property<String> RSDN_USER_ID = create("rsdn.user.id", String.class);
    public static final Property<Boolean> RSDN_USER_PASSWORD_SAVE = create("rsdn.user.password.store", Boolean.class);

    // Janus synchronizator properties

    /**
     * This boolean property indicated is should be used GZip compression while retrieving information from Janus WS.
     */
    public static final Property<Boolean> SERVICE_JANUS_USE_GZIP = create("sunaj.service.janusws.use_gzip", Boolean.class);

    public static final Property<Boolean> SYNCHRONIZER_LOAD_BROKEN_TOPICS_AT_ONCE = create("sunaj.synchronizer.load_boken_topics_at_once", Boolean.class);
    public static final Property<Boolean> SYNCHRONIZER_LOAD_USERS = create("sunaj.synchronizer.load.users", Boolean.class);
    public static final Property<Integer> SYNCHRONIZER_LOAD_USERS_PORTION = create("sunaj.synchronizer.load.users.portion", Integer.class);
    public static final Property<Integer> SYNCHRONIZER_LOAD_MESSAGES_PORTION = create("sunaj.synchronizer.load.messages.portion", Integer.class);
    public static final Property<Boolean> MESSAGE_PANE_SHOW_MARKS = create("sunaj.viewer.show.marks.pane", Boolean.class);

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
        Property<E> prop = new Property<E>(name, type);
        ALL_PROPERTIES.put(name, prop);
        return prop;
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

    private Property(String name, Class<T> type) {
        if (name == null) throw new NullPointerException("Property name can not be null.");
        if (type == null) throw new NullPointerException("Class type can not be null.");

        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<T> getType() {
        return type;
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
