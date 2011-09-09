package org.xblackcat.rojac.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.rojac.service.storage.database.DBStorage;
import org.xblackcat.rojac.service.storage.database.IPropertiable;
import org.xblackcat.rojac.service.storage.database.SQL;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.connection.SimplePooledConnectionFactory;
import org.xblackcat.utils.ResourceUtils;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author xBlackCat
 */

public final class DatabaseUtils {
    private static final Log log = LogFactory.getLog(DatabaseUtils.class);

    private static final String DBCONFIG_PACKAGE = "dbconfig/";
    private static final String DB_STORAGE_JDBC_CLASS = "db.jdbc.driver.class";
    private static final String DB_STORAGE_URL = "db.connection.url.pattern";
    private static final String DB_SHUTDOWN_URL = "db.shutdown.url.pattern";
    private static final String DB_STORAGE_USER = "db.access.user";
    private static final String DB_STORAGE_PASSWORD = "db.access.password";

    private DatabaseUtils() {
    }

    public static <T extends Enum<T> & IPropertiable> Map<T, String> loadSQLs(String propRoot, Class<T> type) throws IOException, StorageInitializationException {
        String name = '/' + DBCONFIG_PACKAGE + propRoot + "/sql.data.properties";
        Properties queries = ResourceUtils.loadProperties(name);

        Map<T, String> qs = new EnumMap<>(type);
        for (T q : type.getEnumConstants()) {
            String sql = (String) queries.remove(q.getPropertyName());
            if (sql != null) {
                if (log.isTraceEnabled()) {
                    log.trace("Property '" + q.getPropertyName() + "' initialized with SQL: " + sql);
                }
                qs.put(q, sql);
            } else {
                throw new StorageInitializationException(q + " is not initialized.");
            }
        }

        if (!queries.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("There are unused properties in " + name);
                for (Map.Entry<Object, Object> entry : queries.entrySet()) {
                    log.warn("Property: " + entry.getKey() + " = " + entry.getValue());
                }
            }
            throw new StorageInitializationException("There are some excess properties in " + name);
        }

        return Collections.unmodifiableMap(qs);
    }

    /**
     * Loads and returns a new Properties object for given resource or path name. The properties in result map stored in
     * the same order as they defined in properties file.
     *
     * @param propertiesFile target properties resource for loading.
     * @return
     * @throws IOException
     */
    public static Map<String, String> loadProperties(String propertiesFile) throws IOException {
        InputStream is;
        try {
            is = ResourceUtils.getResourceAsStream(propertiesFile);
        } catch (MissingResourceException e) {
            if (propertiesFile.toLowerCase().endsWith(".properties")) {
                throw e;
            } else {
                is = ResourceUtils.getResourceAsStream(propertiesFile + ".properties");
            }
        }

        final Map<String, String> map = new LinkedHashMap<>();
        // Workaround to load properties in natural order.
        Properties p = new Properties() {
            @Override
            public Object put(Object key, Object value) {
                map.put(key.toString(), value.toString());
                return super.put(key, value);
            }
        };
        p.load(is);

        return map;
    }

    public static Map<SQL, List<SQL>> loadInitializeSQLs(String propRoot) throws IOException {
        Map<String, String> check = loadProperties('/' + DBCONFIG_PACKAGE + propRoot + "/sql.check.properties");
        Properties init = ResourceUtils.loadProperties('/' + DBCONFIG_PACKAGE + propRoot + "/sql.initialize.properties");
        Properties clue = ResourceUtils.loadProperties('/' + DBCONFIG_PACKAGE + propRoot + "/sql.depends.properties");

        Map<SQL, List<SQL>> map = new LinkedHashMap<>();

        for (Map.Entry<String, String> ce : check.entrySet()) {
            String name = ce.getKey();
            String sql = ce.getValue();

            String inits = clue.getProperty(name, "");
            List<SQL> sqls = new ArrayList<>();
            String[] initNames = inits.trim().split(",");
            if (!ArrayUtils.isEmpty(initNames)) {
                for (String initName : initNames) {
                    String initSql = init.getProperty(initName.trim());
                    if (StringUtils.isBlank(initSql)) {
                        throw new IOException(initName + " SQL not defined (Used in " + name + ").");
                    } else {
                        sqls.add(new SQL(initName, initSql));
                    }
                }
            }

            map.put(new SQL(name, sql), sqls);
        }

        return Collections.unmodifiableMap(map);
    }

    private static DatabaseSettings readDefaults(String configurationName) throws StorageInitializationException {
        if (log.isTraceEnabled()) {
            log.trace("Loading database connection properties.");
        }
        // The properties file should be located in /<propRoot>/database.properties
        Properties databaseProperties;
        try {
            databaseProperties = ResourceUtils.loadProperties(DBCONFIG_PACKAGE + configurationName + "/database.properties");
        } catch (IOException e) {
            throw new StorageInitializationException("Can not load config from the database.properties", e);
        }

        String jdbcClass = databaseProperties.getProperty(DB_STORAGE_JDBC_CLASS);
        if (jdbcClass == null) {
            throw new StorageInitializationException("The " + DB_STORAGE_JDBC_CLASS + " property is not defined.");
        }

        Class<?> jdbcDriverClass;
        String url;
        String userName;
        String password;
        String shutdownUrl;

        try {
            jdbcDriverClass = Class.forName(jdbcClass);
            jdbcDriverClass.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new StorageInitializationException("Can not initialize JDBC driver.", e);
        }

        url = ResourceUtils.putSystemProperties(databaseProperties.getProperty(DB_STORAGE_URL));
        if (url == null) {
            throw new StorageInitializationException("The " + DB_STORAGE_URL + " property is not defined.");
        }

        String shutdownUrlRaw = ResourceUtils.putSystemProperties(databaseProperties.getProperty(DB_SHUTDOWN_URL));

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

        if (StringUtils.isNotBlank(shutdownUrlRaw)) {
            shutdownUrl = ResourceUtils.putSystemProperties(shutdownUrlRaw);
            if (log.isTraceEnabled()) {
                log.trace("Shutdown url: " + shutdownUrl);
            }
        } else {
            shutdownUrl = null;
        }

        return new DatabaseSettings(configurationName, url, shutdownUrl, userName, password, jdbcDriverClass);
    }

    public static String convert(DatabaseSettings o) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);

            ObjectOutputStream datas = new ObjectOutputStream(outputStream);

            datas.writeObject(o.getEngine());
            datas.writeObject(o.getJdbcDriverClass());
            datas.writeObject(o.getUrl());
            datas.writeObject(o.getUserName());
            datas.writeObject(o.getPassword());
            datas.writeObject(o.getShutdownUrl());
            datas.flush();

            return Base64.encodeBase64String(outputStream.toByteArray());
        } catch (Exception e) {
            if (log.isWarnEnabled()) {
                log.warn("Can not store database settings.", e);
            }

            return null;
        }
    }

    public static DatabaseSettings convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        }

        try {
            ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(Base64.decodeBase64(s)));

            String engine = (String) inputStream.readObject();

            // Check if the engine is available
            try {
                ResourceUtils.getResource(DBCONFIG_PACKAGE + engine + "/database.properties");
                ResourceUtils.getResource(DBCONFIG_PACKAGE + engine + "/sql.check.properties");
                ResourceUtils.getResource(DBCONFIG_PACKAGE + engine + "/sql.initialize.properties");
                ResourceUtils.getResource(DBCONFIG_PACKAGE + engine + "/sql.depends.properties");
            } catch (MissingResourceException e) {
                if (log.isWarnEnabled()) {
                    log.warn("Configuration " + engine + " is not valid.");
                }

                return null;
            }

            Class<?> jdbcClass = (Class<?>) inputStream.readObject();

            String url = (String) inputStream.readObject();
            String userName = (String) inputStream.readObject();
            String password = (String) inputStream.readObject();
            String shutdownUrl = (String) inputStream.readObject();

            return new DatabaseSettings(engine, url, shutdownUrl, userName, password, jdbcClass);
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

    public static DBStorage initializeStorage(String defaultEngine) throws RojacException {
        DatabaseSettings settings = Property.ROJAC_DATABASE_CONNECTION_SETTINGS.get();

        String engine;
        if (settings == null) {
            // TODO: show database settings dialog.

            settings = readDefaults(defaultEngine);
            engine = defaultEngine;

            Property.ROJAC_DATABASE_CONNECTION_SETTINGS.set(settings);
        } else {
            engine = settings.getEngine();
        }

        IConnectionFactory connectionFactory = new SimplePooledConnectionFactory(settings);

        DBStorage storage = new DBStorage(engine, connectionFactory);
        storage.initialize();
        return storage;
    }

    @SuppressWarnings({"unchecked"})
    private static IConnectionFactory createConnectionFactory(String connectionFactoryName, DatabaseSettings databaseSettings) throws StorageInitializationException {

        Class<?> connectionFactoryClass;
        try {
            connectionFactoryClass = Class.forName("org.xblackcat.rojac.service.storage.database.connection." + connectionFactoryName + "ConnectionFactory");
        } catch (ClassNotFoundException e) {
            try {
                connectionFactoryClass = Class.forName(connectionFactoryName);
            } catch (ClassNotFoundException e1) {
                throw new StorageInitializationException("Connection factory " + connectionFactoryName + " is not found", e1);
            }
        }

        try {
            if (!IConnectionFactory.class.isAssignableFrom(connectionFactoryClass)) {
                throw new StorageInitializationException("Connection factory should implements IConnectionFactory interface.");
            }
            Constructor<IConnectionFactory> connectionFactoryConstructor =
                    ((Class<IConnectionFactory>) connectionFactoryClass).getConstructor(DatabaseSettings.class);

            return connectionFactoryConstructor.newInstance(databaseSettings);
        } catch (NoSuchMethodException e) {
            throw new StorageInitializationException("Connection factory have no necessary constructor", e);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new StorageInitializationException("Can not initialize connection factory", e);
        }
    }
}
