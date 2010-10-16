package org.xblackcat.rojac.service.options;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

/**
 * Service for retrieving or storing options of the application.
 * <p/>
 *
 * @author Alexey
 */

public final class MultiUserOptionsService extends AnOptionsService {
    private static final Log log = LogFactory.getLog(MultiUserOptionsService.class);
    private static final Collection<Property<?>> DO_NOT_STORE = new HashSet<Property<?>>(Arrays.asList(
            Property.ROJAC_DEBUG_MODE
    ));

    private final String userConfigFileName;

    public MultiUserOptionsService() throws OptionsServiceException {
        String userHome = SystemUtils.USER_HOME;

        if (log.isDebugEnabled()) {
            log.debug("User home dir is " + userHome);
        }
        userConfigFileName = userHome + File.separatorChar + ".rojac" + File.separatorChar + "config.properties";
        File userConfigFile = new File(userConfigFileName);

        if (userConfigFile.exists()) {
            if (log.isDebugEnabled()) {
                log.debug("Use user config file " + userConfigFileName);
            }
            try {
                Properties p = new Properties();
                BufferedInputStream is = new BufferedInputStream(new FileInputStream(userConfigFile));
                p.load(is);
                is.close();
                loadFromResource(p);
            } catch (IOException e) {
                throw new OptionsServiceException("Can not load content of user config file.", e);
            }
        }
    }

    /**
     * Loads from resource the options. If an option have already initialized it will not be overwrited.
     *
     * @param config        config resource bundle.
     */
    private void loadFromResource(Properties config) {
        for (Property<?> p : Property.getAllProperties()) {
            if (getProperty(p) != null) {
                // Property is already set so ignore it.
                continue;
            }

            String key = p.getName();
            String val = config.getProperty(key);
            if (val == null) {
                if (log.isDebugEnabled()) {
                    log.debug("Can not obtain value for the " + p + " from config file.");
                }
                continue;
            }

            // Set property as string.
            if (log.isTraceEnabled()) {
                log.trace("Set to the " + p + " value " + val);
            }
            setProperty(key, val);

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
        if (value == null) {
            return System.clearProperty(key);
        } else {
            return System.setProperty(key, value);
        }
    }

    public boolean storeSettings() {
        Properties userSetting = new Properties();

        File userConfigFile = new File(userConfigFileName);

        for (Property<?> prop : Property.getAllProperties()) {
            String value = getProperty(prop.getName());
            if (!DO_NOT_STORE.contains(prop) && StringUtils.isNotBlank(value)) {
                userSetting.setProperty(prop.getName(), value);
            }
        }

        try {
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(userConfigFile));
            userSetting.store(os, "Rojac user settings file.");
            os.close();
            return true;
        } catch (IOException e) {
            log.error("Can not store user settings.", e);
            return false;
        }
    }
}
