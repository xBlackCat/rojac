package org.xblackcat.sunaj.service.options;

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

    /*
      * Janus synchronizator properties
      */

    /**
     * This boolean property indicated is should be used GZip compression while retrieving information from Janus WS.
     */
    public static final Property<Boolean> SERVICE_JANUS_USE_GZIP = new Property("sunaj.service.janusws.use_gzip", Boolean.class);

    public static final Property<Boolean> SYNCHRONIZER_LOAD_BROKEN_TOPICS_AT_ONCE = new Property<Boolean>("sunaj.synchronizer.load_boken_topics_at_once", Boolean.class);
    public static final Property<Boolean> SYNCHRONIZER_LOAD_USERS = new Property<Boolean>("sunaj.synchronizer.load.users", Boolean.class);
    public static final Property<Integer> SYNCHRONIZER_LOAD_USERS_PORTION = new Property<Integer>("sunaj.synchronizer.load.users.portion", Integer.class);
    public static final Property<Integer> SYNCHRONIZER_LOAD_MESSAGES_PORTION = new Property<Integer>("sunaj.synchronizer.load.messages.portion", Integer.class);

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

    Property(String name, Class<T> type) {
        if (name == null) throw new NullPointerException("Property name can not be null.");
        if (type == null) throw new NullPointerException("Class type can not be null.");

        this.name = name;
        this.type = type;

        ALL_PROPERTIES.put(name, this);
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

        if (!type.equals(property.type)) return false;
        if (!name.equals(property.name)) return false;

        return true;
    }

    public int hashCode() {
        return 31 * name.hashCode() + type.hashCode();
    }

    public String toString() {
        return "Property[" + name + '(' + type.getName() + ")]";
    }
}
