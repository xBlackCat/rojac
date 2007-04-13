package org.xblackcat.sunaj.service.options;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Service for retrieving or storing options of the application.
 * <p/>
 * Date: 13 квіт 2007
 *
 * @author Alexey
 */

public final class MultiUserOptionsService extends AbstractOptionsService {
    private static final Log log = LogFactory.getLog(MultiUserOptionsService.class);

    private static MultiUserOptionsService instance = new MultiUserOptionsService();
    private static final String DEFAULT_CONFIG_BUNDLE_NAME = "/default_config.properties";

    public static IOptionsService getInstance() {
        return instance;
    }

    private String userConfigFileName;

    private MultiUserOptionsService() {
        String userHome = System.getProperty("user.home");
        
        if (log.isDebugEnabled()) {
            log.debug("User home dir is " + userHome);
        }
        userConfigFileName = userHome + File.separatorChar + ".sunaj" + File.separatorChar + "config.properties";
        File userConfigFile = new File(userConfigFileName);

        Property<?>[] allProperties = Property.getAllProperties();
        if (userConfigFile.exists()) {
            if (log.isDebugEnabled()) {
                log.debug("Use user config file " + userConfigFileName);
            }
            try {
                Properties p = new Properties();
                p.load(new FileInputStream(userConfigFile));
                loadFromResource(p, allProperties);
            } catch (IOException e) {
                log.error("Can not load content of user config file.", e);
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("Load default settings if it necessary.");
        }

        try {
            Properties p = new Properties();
            p.load(MultiUserOptionsService.class.getResourceAsStream(DEFAULT_CONFIG_BUNDLE_NAME));
            loadFromResource(p, allProperties);
        } catch (IOException e) {
            log.error("Can not load the defaults.", e);
        }
    }

    /**
     * Loads from resource the options. If an option have already initialized it not overwriting.
     *
     * @param config        config resource bundle.
     * @param allProperties
     */
    private void loadFromResource(Properties config, Property<?>[] allProperties) {
        for (Property<?> p : allProperties) {
            String key = p.getName();
            String val = config.getProperty(key);
            if (val == null) {
                if (log.isWarnEnabled()) {
                    log.warn("Can not obtain value for the " + p + " from config file.");
                }
            }

            if (getProperty(p) == null) {
                // Set property as string.
                if (log.isDebugEnabled()) {
                    log.debug("Set to the " + p + " value " + val);
                }
                setProperty(key, val);
            }

            // Checks the property
            try {
                getProperty(p);
            } catch (RuntimeException e) {
                // Clear the property if it contains the invalid value.
                if (log.isWarnEnabled()) {
                    log.warn(p + " has invalid value - clear it.", e);
                }
                setProperty(key, null);
            }
        }
    }

    protected String getProperty(String key) {
        return System.getProperty(key);
    }

    protected String setProperty(String key, String value) {
        return System.setProperty(key, value);
    }
}
