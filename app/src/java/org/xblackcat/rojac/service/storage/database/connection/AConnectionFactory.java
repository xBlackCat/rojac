package org.xblackcat.rojac.service.storage.database.connection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author xBlackCat
 */

public abstract class AConnectionFactory implements IConnectionFactory {
    protected final Log log;

    private static final String DB_STORAGE_JDBC_CLASS = "db.jdbc.driver.class";
    private static final String DB_STORAGE_URL = "db.connection.url.pattern";
    private static final String DB_SHUTDOWN_URL = "db.shutdown.url.pattern";
    private static final String DB_STORAGE_USER = "db.access.user";
    private static final String DB_STORAGE_PASSWORD = "db.access.password";

    final String url;
    final String shutdownUrl;
    final String userName;
    final String password;

    AConnectionFactory(String configurationName) throws StorageInitializationException {
        log = LogFactory.getLog(getClass());
        if (log.isTraceEnabled()) {
            log.trace("Loading database connection properties.");
        }
        // The properties file should be located in /<propRoot>/database.properties
        Properties databaseProperties;
        try {
            databaseProperties = ResourceUtils.loadProperties(configurationName + "/database.properties");
        } catch (IOException e) {
            throw new StorageInitializationException("Can not load config from the database.properties", e);
        }

        String jdbcClass = databaseProperties.getProperty(DB_STORAGE_JDBC_CLASS);
        if (jdbcClass == null) {
            throw new StorageInitializationException("The " + DB_STORAGE_JDBC_CLASS + " property is not defined.");
        }

        try {
            Class.forName(jdbcClass).newInstance();
        } catch (ClassNotFoundException e) {
            throw new StorageInitializationException("Can not initialize JDBC driver.", e);
        } catch (IllegalAccessException e) {
            throw new StorageInitializationException("Can not initialize JDBC driver.", e);
        } catch (InstantiationException e) {
            throw new StorageInitializationException("Can not initialize JDBC driver.", e);
        }

        String url = databaseProperties.getProperty(DB_STORAGE_URL);
        if (url == null) {
            throw new StorageInitializationException("The " + DB_STORAGE_URL + " property is not defined.");
        }

        String shutdownUrl = databaseProperties.getProperty(DB_SHUTDOWN_URL);

        userName = databaseProperties.getProperty(DB_STORAGE_USER);
        if (userName == null) {
            log.info("The " + DB_STORAGE_USER + " property is not defined. Unrestricted access to database.");
            password = null;
        } else {
            password = databaseProperties.getProperty(DB_STORAGE_PASSWORD);
            if (password == null) {
                log.info("The " + DB_STORAGE_PASSWORD + " property is not defined. Will be used empty password.");
            }
        }

        // Set the system properties in the url string
        if (log.isTraceEnabled()) {
            log.trace("Initial url: " + url);
        }
        url = ResourceUtils.putSystemProperties(url);
        if (log.isTraceEnabled()) {
            log.trace("Url after replace: " + url);
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
    public void shutdown() {
        if (shutdownUrl != null) {
            try {
                DriverManager.getConnection(shutdownUrl);
            } catch (SQLException e) {
                log.error("Can not execute shutdown sequence in DB.", e);
            }
        }
    }
}
