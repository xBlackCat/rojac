package org.xblackcat.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class for working with local resources.
 *
 * @author xBlackCat
 */

public final class ResourceUtils {
    private static final Log log = LogFactory.getLog(ResourceUtils.class);

    private static final Pattern PROPERTY_PATTERN = Pattern.compile("\\{\\$([\\w\\.]+)\\}");

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
        if (s == null) {
            return null;
        }

        boolean tryAgain;
        do {
            tryAgain = false;
            Matcher m = PROPERTY_PATTERN.matcher(s);
            while (m.find()) {
                String property = m.group(1);
                if (log.isTraceEnabled()) {
                    log.trace("Found property " + m.group() + " in the string.");
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
     * Gets resource URL object.
     *
     * @param resourceName
     *
     * @return URL object if resource is found
     *
     * @throws MissingResourceException if recource is not exist
     */
    public static URL getResource(String resourceName) throws MissingResourceException {
        URL url = ResourceUtils.class.getResource(resourceName);
        if (url == null) {
            url = ResourceUtils.class.getClassLoader().getResource(resourceName);
        }
        if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        }
        if (url == null) {
            url = ClassLoader.getSystemResource(resourceName);
        }
        if (url == null) {
            throw new MissingResourceException("Can not find resource " + resourceName, ResourceUtils.class.getName(), resourceName);
        }

        return url;
    }

    /**
     * Loads and returns a new Properties object for given resource or path name.
     *
     * @param propertiesFile
     *
     * @return
     */
    public static Properties loadProperties(String propertiesFile) throws IOException {
        InputStream is;
        try {
            is = getResourceAsStream(propertiesFile);
        } catch (MissingResourceException e) {
            if (propertiesFile.toLowerCase().endsWith(".properties")) {
                throw e;
            } else {
                is = getResourceAsStream(propertiesFile + ".properties");
            }
        }

        Properties p = new Properties();
        p.load(is);

        return p;
    }

    /**
     * Returns stream of specified resource or {@code null} if resource is not exists.
     *
     * @param resourceName resource name to open
     *
     * @return resource stream or null
     *
     * @throws IOException              if input stream can not be opened
     * @throws MissingResourceException if specified resource can not be found.
     */
    public static InputStream getResourceAsStream(String resourceName) throws IOException, MissingResourceException {
        return getResource(resourceName).openStream();
    }

    /**
     * Loads a list of strings from a file in resources. The method assumes that file contines one string per line.
     *
     * @param resourceName
     *
     * @return array of strings.
     *
     * @throws IOException
     * @throws MissingResourceException
     */
    public static String[] loadListFromResource(String resourceName) throws IOException, MissingResourceException {
        InputStream stream = getResourceAsStream(resourceName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        List<String> strings = new LinkedList<>();

        String s;
        while ((s = reader.readLine()) != null) {
            strings.add(s.trim());
        }
        return strings.toArray(new String[strings.size()]);
    }

    /**
     * Loads object by its FQN. The path can be either to object class or enum constant field.
     *
     * @param aClassName FQN of class or enum constant
     *
     * @return loaded object.
     *
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object loadObjectOrEnum(String aClassName) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
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

    /**
     * Loads an image from resource.
     *
     * @param path image location
     *
     * @return loaded image.
     *
     * @throws MissingResourceException
     */
    public static ImageIcon loadImageIcon(String path) throws MissingResourceException {
        return new ImageIcon(getResource(path));
    }

    public static Icon loadIcon(String path) throws MissingResourceException {
        return loadImageIcon(path);
    }

    public static Image loadImage(String path) throws MissingResourceException {
        ImageIcon ii = loadImageIcon(path);

        return ii == null ? null : ii.getImage();
    }

    /**
     * Returns full path to resource to access to file from an external storage.
     *
     * @param path resource FQN.
     *
     * @return full file name.
     *
     * @throws MissingResourceException
     */
    public static String getFullPathToResource(String path) throws MissingResourceException {
        return getResource(path).toString();
    }
}
