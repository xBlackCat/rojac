package org.xblackcat.sunaj.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

public class ResourceUtils {
    private static final Log log = LogFactory.getLog(ResourceUtils.class);

    private static final Pattern PROPERTY_PATTERN = Pattern.compile("\\{\\$([\\w\\.]+)\\}");
    private static final Pattern TO_KEY_PATTERN = Pattern.compile("_");

    private ResourceUtils() {
    }

    /**
     * Replaces the {$system properties} in the given string with its values.
     *
     * @param s the string to put system properties.
     *
     * @return a new string with put system properties.
     */
    public static String putSystemProperties(String s) {
        boolean tryAgain;
        do {
            tryAgain = false;
            Matcher m = PROPERTY_PATTERN.matcher(s);
            while (m.find()) {
                String property = m.group(1);
                if (log.isTraceEnabled()) {
                    log.trace("Found property " + m.group() + " in the url.");
                }
                String val = System.getProperty(property);
                if (log.isTraceEnabled()) {
                    log.trace(m.group() + " = " + val);
                }
                if (val != null) {
                    val = val.replace(File.separatorChar, '/');
                    s = s.replaceAll("\\{\\$" + property + "\\}", val);
                    tryAgain = true;
                }
            }
        } while (tryAgain);
        return s;
    }

    /**
     * Converts constant names to equivalent property key. For example, constant name <code>EXAMPLE_NAME</code> will
     * converted to <code>example.name</code>. If specified name is <code>null</code> the <code>null</code> will
     * returned.
     *
     * @param name name to convert.
     *
     * @return converted name.
     */
    public static String constantToProperty(String name) {
        if (name == null) {
            return null;
        }

        return TO_KEY_PATTERN.matcher(name.toLowerCase()).replaceAll(".");
    }

    public static URL getResource(String r) throws MissingResourceException {
        URL url = ResourceUtils.class.getResource(r);
        if (url == null) {
            url = ResourceUtils.class.getClassLoader().getResource(r);
        }
        if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(r);
        }
        if (url == null) {
            url = ClassLoader.getSystemResource(r);
        }
        if (url == null) {
            throw new MissingResourceException("Can not find resource " + r, ResourceUtils.class.getName(), r);
        }

        return url;
    }

    /**
     * Returns stream of specified resource or <code>null</code> if resource is not exists.
     *
     * @param r
     *
     * @return
     *
     * @throws IOException
     */
    public static InputStream getResourceAsStream(String r) throws IOException, MissingResourceException {
        return getResource(r).openStream();
    }

    public static String[] loadListFromResource(String resourceName) throws IOException, MissingResourceException {
        InputStream stream = getResourceAsStream(resourceName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        List<String> strings = new LinkedList<String>();

        String s;
        while ((s = reader.readLine()) != null) {
            strings.add(s);
        }
        return strings.toArray(new String[strings.size()]);
    }

    /**
     * Loads object by its FQN.
     *
     * @param aClassName FQN of class or enum constant
     *
     * @return loaded object.
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object loadClassOrEnum(String aClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        final ClassLoader loader = ResourceUtils.class.getClassLoader();
        Class<?> aClass;
        try {
            aClass = loader.loadClass(aClassName);
            return aClass.newInstance();
        } catch (ClassNotFoundException e) {
            final int i = aClassName.lastIndexOf('.');
            String enumConst = aClassName.substring(i + 1);
            String aSubClassName = aClassName.substring(0, i);

            try {
                aClass = loader.loadClass(aSubClassName);

                final Field field = aClass.getDeclaredField(enumConst);
                if (field.isEnumConstant()) {
                    return field.get(null);
                }
            } catch (ClassNotFoundException e1) {
                throw new ClassNotFoundException("Either " + aClassName + " nor " + aSubClassName + " can not be found");
            } catch (NoSuchFieldException e1) {
                throw new ClassNotFoundException("There is no field " + enumConst + " in a " + aSubClassName + " class");
            } catch (IllegalAccessException e1) {
                throw new ClassNotFoundException("Can not access to the field " + enumConst + " in a " + aSubClassName + " class");
            }
        }
        return aClass;
    }

    public static ImageIcon loadImageIcon(String path) throws MissingResourceException {
        return new ImageIcon(getResource(path));
    }

    public static String getFullPathToResource(String path) throws MissingResourceException {
        return getResource(path).toString();
    }

    public static String constantToPropertyName(String s) {
        return s.toLowerCase().replace('_', '.');
    }
}
