package org.xblackcat.rojac.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.IRSDNable;
import org.xblackcat.rojac.gui.dialogs.PropertyNode;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

public final class RojacUtils {
    private static final Log log = LogFactory.getLog(RojacUtils.class);

    public static final String VERSION = "0.1alpha";
    public static final String VERSION_STRING;

    static {
        StringBuilder versionString = new StringBuilder("Rojac v");
        versionString.append(VERSION);

        try {
            Pattern propNamePattern = Pattern.compile("([\\w\\s]+): (.+)");

            BufferedReader revData = new BufferedReader(new InputStreamReader(ResourceUtils.getResourceAsStream("config/rojac.revision")));
            //Prepare content to be read.
            StringBuilder newContent = new StringBuilder();
            String s;
            while ((s = revData.readLine()) != null) {
                Matcher m = propNamePattern.matcher(s);
                if (m.matches()) {
                    newContent.append(m.group(1).replace(' ', '-'));
                    newContent.append('=');
                    newContent.append(m.group(2));
                    newContent.append('\n');
                }
            }

            Properties revInfo = new Properties();
            revInfo.load(new StringReader(newContent.toString()));

            // Remember - we are working only with build.xml revision!
            String revNum = revInfo.getProperty("Revision");
            String path = revInfo.getProperty("Repository-Root");
            String file = revInfo.getProperty("URL");

            // Now fill additional info

            versionString.append(" (rev. ");
            versionString.append(revNum);

            if (file.startsWith(path)) {
                int pos = path.length() + 1;
                int nextPos = file.indexOf('/', pos);

                String branch = file.substring(pos, nextPos);
                if ("trunk".equals(branch)) {
                    // No additional info will be added
                } else if ("branches".equals(branch)) {
                    // Read branch name
                    pos = nextPos + 1;
                    nextPos = file.indexOf('/', pos);

                    versionString.append(" [");
                    versionString.append(file.substring(pos, nextPos));
                    versionString.append(']');
                }
            }
            versionString.append(')');
        } catch (IOException e) {
            throw new RuntimeException("It finally happened!", e);
        } catch (MissingResourceException e) {
            // No resource is available - do not append revision number
        }
        VERSION_STRING = versionString.toString();

    }

    private RojacUtils() {
    }

    public static <T extends Serializable> T[] getRSDNObject(IRSDNable<T>[] ar) {
        Class<T> c = (Class<T>) ((ParameterizedType) ar.getClass().getComponentType().getGenericInterfaces()[0]).getActualTypeArguments()[0];

        List<T> res = new ArrayList<T>(ar.length);

        for (IRSDNable<T> o : ar) {
            res.add(o.getRSDNObject());
        }

        T[] a = (T[]) Array.newInstance(c, ar.length);
        return res.toArray(a);
    }

    public static String constructDebugSQL(String sql, Object... parameters) {
        String query = sql;

        for (Object value : parameters) {
            String str;
            if (value instanceof String) {
                str = "'" + Matcher.quoteReplacement(value.toString()) + "'";
            } else {
                str = Matcher.quoteReplacement(value.toString());
            }
            query = query.replaceFirst("\\?", str);
        }

        return query;
    }

    /*
    * Util methods for converting values.
    */
    public static <T extends Enum<T>> T convertToEnum(Class<T> enumClass, String val) {
        if (val != null) {
            return Enum.valueOf(enumClass, val);
        } else {
            return null;
        }
    }

    public static void setLookAndFeel(LookAndFeel laf) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(laf);

        for (Frame f : Frame.getFrames()) {
            SwingUtilities.updateComponentTreeUI(f);
        }
    }

    /**
     * Generates a tree path of the specified property by its name.
     *
     * @param p
     *
     * @return root of the path.
     */
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

        return false;
    }

    public static LookAndFeel getDefaultLAFClass() {
        String lafClassName = UIManager.getSystemLookAndFeelClassName();

        LookAndFeel laf = null;
        try {
            laf = (LookAndFeel) ResourceUtils.loadObjectOrEnum(lafClassName);
        } catch (ClassNotFoundException e) {
            log.debug("System L&F class " + lafClassName + " not found.", e);
        } catch (IllegalAccessException e) {
            log.debug("System L&F class " + lafClassName + " can not be accessed.", e);
        } catch (InstantiationException e) {
            log.debug("System L&F class " + lafClassName + " can not be initialized.", e);
        }

        if (laf == null) {
            laf = new MetalLookAndFeel();
        }

        return laf;
    }
}
