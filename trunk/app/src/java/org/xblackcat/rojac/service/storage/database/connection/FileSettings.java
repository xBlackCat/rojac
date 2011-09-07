package org.xblackcat.rojac.service.storage.database.connection;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.rojac.util.QueryUtils;
import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author xBlackCat
 */
public class FileSettings implements ISettings {
    private static final Log log = LogFactory.getLog(FileSettings.class);

    private static final String DB_STORAGE_JDBC_CLASS = "db.jdbc.driver.class";
    private static final String DB_STORAGE_URL = "db.connection.url.pattern";
    private static final String DB_SHUTDOWN_URL = "db.shutdown.url.pattern";
    private static final String DB_STORAGE_USER = "db.access.user";
    private static final String DB_STORAGE_PASSWORD = "db.access.password";

    private final String url;
    private final String shutdownUrl;
    private final String userName;
    private final String password;

    public FileSettings(String configurationName) throws StorageInitializationException {
        if (log.isTraceEnabled()) {
            log.trace("Loading database connection properties.");
        }
        // The properties file should be located in /<propRoot>/database.properties
        Properties databaseProperties;
        try {
            databaseProperties = ResourceUtils.loadProperties(QueryUtils.DBCONFIG_PACKAGE + configurationName + "/database.properties");
        } catch (IOException e) {
            throw new StorageInitializationException("Can not load config from the database.properties", e);
        }

        String jdbcClass = databaseProperties.getProperty(DB_STORAGE_JDBC_CLASS);
        if (jdbcClass == null) {
            throw new StorageInitializationException("The " + DB_STORAGE_JDBC_CLASS + " property is not defined.");
        }

        try {
            Class.forName(jdbcClass).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new StorageInitializationException("Can not initialize JDBC driver.", e);
        }

        String url = ResourceUtils.putSystemProperties(databaseProperties.getProperty(DB_STORAGE_URL));
        if (url == null) {
            throw new StorageInitializationException("The " + DB_STORAGE_URL + " property is not defined.");
        }

        String shutdownUrl = ResourceUtils.putSystemProperties(databaseProperties.getProperty(DB_SHUTDOWN_URL));

        userName = ResourceUtils.putSystemProperties(databaseProperties.getProperty(DB_STORAGE_USER));
        if (userName == null) {
            log.info("The " + DB_STORAGE_USER + " property is not defined. Unrestricted access to database.");
            password = null;
        } else {
            password = ResourceUtils.putSystemProperties(databaseProperties.getProperty(DB_STORAGE_PASSWORD));
            if (password == null) {
                log.info("The " + DB_STORAGE_PASSWORD + " property is not defined. Will be used empty password.");
            }
        }

        if (StringUtils.isNotBlank(shutdownUrl)) {
            this.shutdownUrl = ResourceUtils.putSystemProperties(shutdownUrl);
            if (log.isTraceEnabled()) {
                log.trace("Shutdown url: " + this.shutdownUrl);
            }
        } else {
            this.shutdownUrl = null;
        }

        this.url = url;

    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getShutdownUrl() {
        return shutdownUrl;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
