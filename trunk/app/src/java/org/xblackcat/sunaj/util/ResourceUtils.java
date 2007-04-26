package org.xblackcat.sunaj.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
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
        boolean tryAgaing;
        do {
            tryAgaing = false;
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
                    tryAgaing = true;
                }
            }
        } while (tryAgaing);
        return s;
    }
}
