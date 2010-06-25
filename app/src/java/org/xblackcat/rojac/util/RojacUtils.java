package org.xblackcat.rojac.util;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.xblackcat.rojac.data.IRSDNable;
import org.xblackcat.rojac.gui.dialogs.PropertyNode;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xBlackCat
 */

@SuppressWarnings({"unchecked"})
public final class RojacUtils {
    private static final Log log = LogFactory.getLog(RojacUtils.class);

    public static final String VERSION = "0.1";
    public static final String VERSION_MODIFIER = "alpha";
    public static final String VERSION_STRING;
    public static final GlobalExceptionHandler GLOBAL_EXCEPTION_HANDLER = new GlobalExceptionHandler();

    static {
        StringBuilder versionString = new StringBuilder("Rojac v");
        versionString.append(VERSION);

        if (StringUtils.isNotEmpty(VERSION_MODIFIER)) {
            versionString.append(" (");
            versionString.append(VERSION_MODIFIER);
            versionString.append(")");
        }

        try {
            Properties revInfo = new Properties();
            revInfo.load(ResourceUtils.getResourceAsStream("config/rojac.revision"));

            // Remember - we are working only with build.xml revision!
            String revNum = revInfo.getProperty("revision");
            String file = revInfo.getProperty("relative.path");

            // Now fill additional info

            int pos = 1;
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
            } else if ("tags".equals(branch)) {
                // Read branch name
                pos = nextPos + 1;
                nextPos = file.indexOf('/', pos);

                versionString.append(" <");
                versionString.append(file.substring(pos, nextPos));
                versionString.append('>');
            }

            versionString.append(" / rev. ");
            versionString.append(revNum);
        } catch (IOException e) {
            throw new RuntimeException("It finally happened!", e);
        } catch (MissingResourceException e) {
            // No resource is available - do not append revision number
        }
        VERSION_STRING = versionString.toString();

    }

    private RojacUtils() {
    }

    @SuppressWarnings({"unchecked"})
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

    /**
     * Returns a list of available locales for specified bundle.
     *
     * @param bundle base name of bundle with / as separators and without extension.
     *
     * @return an array of available locales for the specified bundle.
     *
     * @throws IOException if bundle list can not be obtained.
     */
    public static Locale[] localesForBundle(String bundle) throws IOException {
        return localesForBundle(bundle, false);
    }

    /**
     * Returns a list of available locales for specified bundle.
     *
     * @param bundle     base name of bundle with / as separators and without extension.
     * @param addDefault flag to specify is the default resource bundle should be associated with default locale.
     *
     * @return an array of available locales for the specified bundle.
     *
     * @throws IOException if bundle list can not be obtained.
     */
    public static Locale[] localesForBundle(String bundle, boolean addDefault) throws IOException {
        if (bundle == null) {
            throw new NullPointerException("Empty bundle name");
        }

        // Prepare bundle string for processing.
        while (bundle.startsWith("/")) {
            bundle = bundle.substring(1);
        }

        while (bundle.endsWith("/")) {
            bundle = bundle.substring(0, bundle.length() - 1);
        }

        if (bundle.length() == 0) {
            throw new IllegalArgumentException("Empty bundle name.");
        }

        String bundleName;

        int pos = bundle.lastIndexOf('/');
        if (pos == -1) {
            bundleName = bundle;
        } else {
            bundleName = bundle.substring(pos + 1);
        }

        Collection<Locale> locales = new HashSet<Locale>();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = resolver.getResources("/" + bundle + "*.properties");
        Pattern localePattern = Pattern.compile(Pattern.quote(bundleName) + "(?:_(\\w{2})(?:_(\\w{2}))?)?\\.properties");
        for (Resource resource : resources) {
            Matcher m = localePattern.matcher(resource.getFilename());
            if (m.matches()) {
                if (m.group(1) == null) {
                    if (addDefault) {
                        locales.add(Locale.getDefault());
                    }
                } else if (m.group(2) == null) {
                    locales.add(new Locale(m.group(1)));
                } else {
                    locales.add(new Locale(m.group(1), m.group(2)));
                }
            } else {
                System.out.println("Something wrong.");
            }
        }

        return locales.toArray(new Locale[locales.size()]);
    }

    /**
     * Util class for checking if the method executed in SwingThread or not.
     *
     * @param swing     control flag. If set to <code>true</code> the current method will check if it is executing in
     *                  the EventDispatching thread. If set to <code>false</code> - in the non-EventDispatching thread.
     *                  If condition is failed a message will be added into logs.
     * @param tillClass class object till which a stack trace should be trimmed.
     */
    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    public static void checkThread(boolean swing, Class<?> tillClass) {
        if (swing != EventQueue.isDispatchThread()) {
            Throwable stack = new Throwable("Stack trace");
            StackTraceElement[] stackTrace = stack.getStackTrace();

            if (tillClass != null) {
                int shift = 0;
                while (!tillClass.getName().equals(stackTrace[shift].getClassName())) {
                    shift++;
                }

                while (tillClass.getName().equals(stackTrace[shift].getClassName())) {
                    shift++;
                }

                if (shift != stackTrace.length) {
                    stack.setStackTrace((StackTraceElement[]) ArrayUtils.subarray(stackTrace, shift, stackTrace.length - shift - 1));
                }
            }

            if (log.isWarnEnabled()) {
                log.warn(swing ?
                        "The method is not executed in EventQueue!" :
                        "The method is executed in EventQueue!",
                        stack
                );
            }
        }
    }

    public static void showExceptionDialog(Throwable e) {
        showExceptionDialog(Thread.currentThread(), e);
    }

    public static void showExceptionDialog(Thread t, Throwable e) {

        GLOBAL_EXCEPTION_HANDLER.uncaughtException(t, e);
    }

    private static class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(final Thread t, final Throwable e) {
            log.error("Got unhandled exception in " + t, e);

            if (EventQueue.isDispatchThread()) {
                DialogHelper.showExceptionDialog(t, e);
            } else {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        DialogHelper.showExceptionDialog(t, e);
                    }
                });
            }
        }
    }
}
