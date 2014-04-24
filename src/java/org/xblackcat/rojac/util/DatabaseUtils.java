package org.xblackcat.rojac.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.sjpu.storage.connection.DBConfig;
import org.xblackcat.utils.ResourceUtils;

import java.io.*;
import java.util.MissingResourceException;
import java.util.Properties;

/**
 * @author xBlackCat
 */

public final class DatabaseUtils {
    private static final Log log = LogFactory.getLog(DatabaseUtils.class);

    private static final String DB_STORAGE_JDBC_CLASS = "db.jdbc.driver.class";
    private static final String DB_STORAGE_URL = "db.connection.url.pattern";
    private static final String DB_STORAGE_USER = "db.access.user";
    private static final String DB_STORAGE_PASSWORD = "db.access.password";

    private DatabaseUtils() {
    }

    public static DBConfig readDefaults() throws StorageInitializationException {
        if (log.isTraceEnabled()) {
            log.trace("Loading database connection properties.");
        }
        // The properties file should be located in /<propRoot>/database.properties
        Properties databaseProperties;
        try {
            databaseProperties = ResourceUtils.loadProperties("/database.properties");
        } catch (IOException e) {
            throw new StorageInitializationException("Can not load config from the database.properties", e);
        }

        String jdbcClass = databaseProperties.getProperty(DB_STORAGE_JDBC_CLASS);
        if (jdbcClass == null) {
            throw new StorageInitializationException("The " + DB_STORAGE_JDBC_CLASS + " property is not defined.");
        }

        String url;
        String userName;
        String password;

        url = ResourceUtils.putSystemProperties(databaseProperties.getProperty(DB_STORAGE_URL));
        if (url == null) {
            throw new StorageInitializationException("The " + DB_STORAGE_URL + " property is not defined.");
        }

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


        return new DBConfig(
                jdbcClass,
                ResourceUtils.putSystemProperties(url),
                ResourceUtils.putSystemProperties(userName),
                ResourceUtils.putSystemProperties(password),
                10
        );
    }

    public static String convert(DBConfig o) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);

            ObjectOutputStream datas = new ObjectOutputStream(outputStream);

            datas.writeObject(o.getDriver());
            datas.writeObject(o.getUrl());
            datas.writeObject(o.getUser());
            datas.writeObject(o.getPassword());
            datas.flush();

            return Base64.encodeBase64String(outputStream.toByteArray());
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not store database settings.", e);
            }

            return null;
        }
    }

    public static DBConfig convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }

        try {
            ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(Base64.decodeBase64(s)));

            String engine = (String) inputStream.readObject();

            // Check if the engine is available
            try {
                ResourceUtils.getResource("dbconfig/" + engine + "/database.properties");
                ResourceUtils.getResource("dbconfig/" + engine + "/sql.check.properties");
                ResourceUtils.getResource("dbconfig/" + engine + "/sql.initialize.properties");
                ResourceUtils.getResource("dbconfig/" + engine + "/sql.depends.properties");
            } catch (MissingResourceException e) {
                if (log.isWarnEnabled()) {
                    log.warn("Configuration " + engine + " is not valid.");
                }

                return null;
            }

            String jdbcClass = (String) inputStream.readObject();
            String url = (String) inputStream.readObject();
            String userName = (String) inputStream.readObject();
            String password = (String) inputStream.readObject();

            return new DBConfig(jdbcClass, url, userName, password, 10);
        } catch (IOException e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not load database settings.");
            }
        } catch (ClassNotFoundException e) {
            if (log.isWarnEnabled()) {
                log.warn("Database driver is not found.", e);
            }
        }

        return null;
    }

}
