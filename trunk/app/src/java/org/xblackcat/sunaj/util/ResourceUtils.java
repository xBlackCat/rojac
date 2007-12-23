package org.xblackcat.sunaj.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.URL;
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

    static URL getResource(String r) {
        URL url = ImageUtils.class.getResource(r);
        if (url == null) {
            url = ImageUtils.class.getClassLoader().getResource(r);
        }
        if (url == null) {
            url = Thread.currentThread().getContextClassLoader().getResource(r);
        }
        if (url == null) {
            url = ClassLoader.getSystemResource(r);
        }

        return url;
    }
}
