package org.xblackcat.rojac.gui.dialog.options;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.xblackcat.rojac.service.options.Property;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.Iterator;

/**
 * @author xBlackCat
 */

public final class PropertyUtils {
    private PropertyUtils() {
    }

    public static <T extends Enum<T>> T toEnum(Class<T> enumClass, String val) {
        if (val != null) {
            return Enum.valueOf(enumClass, val);
        } else {
            return null;
        }
    }

    /**
     * Generates a tree path of the specified property by its name.
     *
     * @param p property object to generate a path of.
     * @return root of the path.
     */
    @SuppressWarnings({"unchecked"})
    public static PropertyNode propertyPath(Property p) {
        String propertyName = p.getName();

        String[] names = propertyName.split("\\.+");

        if (ArrayUtils.isEmpty(names)) {
            return null;
        }

        if (names.length == 1) {
            return new PropertyNode(names[0], null, p);
        }

        PropertyNode[] nodes = new PropertyNode[names.length];

        nodes[0] = new PropertyNode(names[0]);

        int i = 1, lastNode = names.length - 1;
        while (i < lastNode) {
            String name = names[i];

            nodes[i] = new PropertyNode(name, nodes[i - 1]);
            nodes[i - 1].addChild(nodes[i]);

            i++;
        }

        PropertyNode lastParent = nodes[names.length - 2];
        lastParent.addChild(new PropertyNode(names[lastNode], lastParent, p));

        return nodes[0];
    }

    @SuppressWarnings({"unchecked"})
    public static boolean addProperty(PropertyNode root, Property p) {
        if (root == null) {
            throw new NullPointerException("Node root is null");
        }

        PropertyNode path = propertyPath(p);

        if (!path.equals(root)) {
            return false;
        }

        if (path.isEmpty()) {
            return false;
        }

        do {
            // Assumes that path have only one child or no one.
            path = path.getChild(0);

            if (!root.has(path)) {
                root.addChild(path);
                return true;
            }

            root = root.getChild(root.indexOf(path));
        } while (!path.isEmpty());

        if (root.getProperty() == null) {
            return root.setProperty(path);
        }

        return false;
    }

    @SuppressWarnings({"unchecked"})
    public static EnumSet toEnumSet(EnumSet keyDefault, String val) {
        if (val == null) {
            return null;
        }

        EnumSet set = EnumSet.copyOf(keyDefault);
        set.clear();

        if (StringUtils.isBlank(val)) {
            return set;
        }

        // A little hack with enum sets
        Class<Enum> enumClass;
        try {
            Field elementType = EnumSet.class.getDeclaredField("elementType");
            elementType.setAccessible(true);
            enumClass = (Class<Enum>) elementType.get(keyDefault);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("EnumSet is changed!", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Can not get necessary info!!", e);
        }

        String[] elements = StringUtils.split(val, '+');
        for (String element : elements) {
            try {
                Enum e = Enum.valueOf(enumClass, StringUtils.trimToEmpty(element));
                set.add(e);
            } catch (Exception e1) {
                // Just skip invalid value
            }
        }

        return set;
    }

    public static <T extends Enum<T>> String toString(EnumSet<T> o) {
        if (o.isEmpty()) {
            return "";
        }

        Iterator<T> i = o.iterator();
        StringBuilder res = new StringBuilder(i.next().name());
        while (i.hasNext()) {
            res.append('+');
            res.append(i.next().name());
        }
        return res.toString();
    }
}
