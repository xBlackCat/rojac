package org.xblackcat.rojac.service.storage.database.connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * @author xBlackCat
 */

public abstract class AConnectionFactory implements IConnectionFactory {
    private static final Log log = LogFactory.getLog(AConnectionFactory.class);

    private static final String DSSTORAGE_JDBC_CLASS = "db.jdbc.driver.class";
    private static final String DSSTORAGE_URL = "db.connection.url.pattern";
    private static final String DSSTORAGE_USER = "db.access.user";
    private static final String DSSTORAGE_PASSWORD = "db.access.password";

    protected final String url;
    protected final String userName;
    protected final String password;

    public AConnectionFactory(String configurationName) throws StorageInitializationException {
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

        String jdbcClass = databaseProperties.getProperty(DSSTORAGE_JDBC_CLASS);
        if (jdbcClass == null) {
            throw new StorageInitializationException("The " + DSSTORAGE_JDBC_CLASS + " property is not defined.");
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

        String url = databaseProperties.getProperty(DSSTORAGE_URL);
        if (url == null) {
            throw new StorageInitializationException("The " + DSSTORAGE_URL + " property is not defined.");
        }

        userName = databaseProperties.getProperty(DSSTORAGE_USER);
        if (userName == null) {
            throw new StorageInitializationException("The " + DSSTORAGE_USER + " property is not defined.");
        }

        password = databaseProperties.getProperty(DSSTORAGE_PASSWORD);
        if (password == null) {
            throw new StorageInitializationException("The " + DSSTORAGE_PASSWORD + " property is not defined.");
        }

        // Set the system properties in the url string
        if (log.isTraceEnabled()) {
            log.trace("Initial url: " + url);
        }
        url = ResourceUtils.putSystemProperties(url);
        if (log.isTraceEnabled()) {
            log.trace("Url after replace: " + url);
        }

        this.url = url;
    }
}
