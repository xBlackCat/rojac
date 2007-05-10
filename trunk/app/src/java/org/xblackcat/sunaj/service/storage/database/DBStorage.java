package org.xblackcat.sunaj.service.storage.database;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.service.storage.*;
import org.xblackcat.sunaj.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.sunaj.service.storage.database.connection.SimpleConnectionFactory;
import org.xblackcat.sunaj.service.storage.database.convert.IToObjectConvertor;
import org.xblackcat.sunaj.service.storage.database.convert.ToScalarConvertor;
import org.xblackcat.sunaj.service.storage.database.helper.IQueryHelper;
import org.xblackcat.sunaj.service.storage.database.helper.QueryHelper;
import org.xblackcat.sunaj.util.ResourceUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public class DBStorage implements IStorage, IQueryExecutor {
    private static final Log log = LogFactory.getLog(DBStorage.class);

    private static final String DSSTORAGE_JDBC_CLASS = "db.jdbc.driver.class";
    private static final String DSSTORAGE_URL = "db.connection.url.pattern";
    private static final String DSSTORAGE_USER = "db.access.user";
    private static final String DSSTORAGE_PASSWORD = "db.access.password";

    private final Map<DataQuery, String> queries;
    private final Map<CheckQuery, String> checkQueries;
    private final Map<InitializeQuery, String> initializeQueries;

    private final IQueryHelper helper;
    
    private final DBForumDAO forumDAO;
    private final DBRatingDAO ratingDAO;
    private final DBUserDAO userDAO;
    private final DBForumGroupDAO forumGroupDAO;
    private final DBVersionDAO versionDAO;
    private final DBNewRatingDAO newRatingDAO;
    private final DBModerateDAO moderateDAO;
    private final DBNewMessageDAO newMessageDAO;
    private final DBMessageDAO messageDAO;

    public DBStorage(String propRoot) throws StorageException {
        try {
            IConnectionFactory cf = initializeConnectionFactory('/' + propRoot + "/database.properties");

            this.queries = loadSQLs('/' + propRoot + "/sql.data.properties", DataQuery.class);
            this.checkQueries = loadSQLs('/' + propRoot + "/sql.check.properties", CheckQuery.class);
            this.initializeQueries = loadSQLs('/' + propRoot + "/sql.initialize.properties", InitializeQuery.class);

            helper = new QueryHelper(cf);
        } catch (StorageInitializationException e) {
            throw e;
        } catch (IOException e) {
            throw new StorageInitializationException("Can not setup storage factory.", e);
        } catch (Exception e) {
            throw new StorageInitializationException("Unspecified exception occurs while DB storage initializating.", e);
        }

        // Initialize the object DAOs
        forumDAO = new DBForumDAO(this);
        ratingDAO = new DBRatingDAO(this);
        userDAO = new DBUserDAO(this);
        forumGroupDAO = new DBForumGroupDAO(this);
        versionDAO = new DBVersionDAO(this);
        newRatingDAO = new DBNewRatingDAO(this);
        moderateDAO = new DBModerateDAO(this);
        newMessageDAO = new DBNewMessageDAO(this);
        messageDAO = new DBMessageDAO(this);
    }

    /* Initialization routines */
    public boolean checkStructure() {
        if (log.isInfoEnabled()) {
            log.info("Check database storage structure started.");
        }
        for (Map.Entry<CheckQuery, String> entry : checkQueries.entrySet()) {
            if (log.isDebugEnabled()) {
                log.debug("Checking: " + entry.getKey());
            }
            try {
                Boolean c = helper.executeSingle(new ToScalarConvertor<Boolean>(), entry.getValue());
                if (!Boolean.TRUE.equals(c)) {
                    // If c is null or FALSE - abort.
                    if (log.isDebugEnabled()) {
                        log.debug(entry.getKey() + " check failed.");
                    }
                    return false;
                }
            } catch (StorageException e) {
                if (log.isDebugEnabled()) {
                    log.debug(entry.getKey() + " check failed.", e);
                }
                return false;
            }
        }
        return true;
    }

    public void initialize() throws StorageException {
        if (log.isInfoEnabled()) {
            log.info("The storage initialization started.");
        }
        for (Map.Entry<InitializeQuery, String> e : initializeQueries.entrySet()) {
            if (log.isDebugEnabled()) {
                log.debug("Performing: " + e.getKey());
            }
            try {
                helper.update(e.getValue());
            } catch (StorageException $) {
                if (log.isWarnEnabled()) {
                    log.warn("Can not perform initialization step: " + e.getKey(), $);
                }
            }
        }
    }

    public IForumDAO getForumDAO() {
        return forumDAO;
    }

    public IForumGroupDAO getForumGroupDAO() {
        return forumGroupDAO;
    }

    public IMessageDAO getMessageDAO() {
        return messageDAO;
    }

    public IModerateDAO getModerateDAO() {
        return moderateDAO;
    }

    public INewMessageDAO getNewMessageDAO() {
        return newMessageDAO;
    }

    public INewRatingDAO getNewRatingDAO() {
        return newRatingDAO;
    }

    public IRatingDAO getRatingDAO() {
        return ratingDAO;
    }

    public IUserDAO getUserDAO() {
        return userDAO;
    }

    public IVersionDAO getVersionDAO() {
        return versionDAO;
    }

    public int update(DataQuery sql, Object... params) throws StorageException {
        return helper.update(getQuery(sql), params);
    }

    public <T> T executeSingle(IToObjectConvertor<T> c, DataQuery sql, Object... params) throws StorageException {
        return helper.executeSingle(c, getQuery(sql), params);
    }

    public <T> Collection<T> execute(IToObjectConvertor<T> c, DataQuery sql, Object... params) throws StorageException {
        return helper.execute(c, getQuery(sql), params);
    }

    public int[] getIds(DataQuery sql, Object... params) throws StorageException {
        Collection<Integer> objIds = execute(new ToScalarConvertor<Integer>(), sql, params);
        int[] ids;

        try {
            // Conver collection of Integer to array of int.
            ids = ArrayUtils.toPrimitive(objIds.toArray(new Integer[objIds.size()]));
        } catch (NullPointerException e) {
            throw new StorageDataException("Got null instead of real value.", e);
        }

        return ids;
    }

    protected String getQuery(DataQuery q) {
        return queries.get(q);
    }

    protected String getQuery(CheckQuery q) {
        return checkQueries.get(q);
    }

    protected String getQuery(InitializeQuery q) {
        return initializeQueries.get(q);
    }

    private IConnectionFactory initializeConnectionFactory(String name) throws StorageInitializationException {
        if (log.isDebugEnabled()) {
            log.debug("Loading database connection properties.");
        }
        // The properties file should be located in /<propRoot>/database.properties
        Properties databaseProperties = new Properties();
        try {
            databaseProperties.load(getClass().getResourceAsStream(name));
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

        String dbUser = databaseProperties.getProperty(DSSTORAGE_USER);
        if (dbUser == null) {
            throw new StorageInitializationException("The " + DSSTORAGE_USER + " property is not defined.");
        }

        String dbPassword = databaseProperties.getProperty(DSSTORAGE_PASSWORD);
        if (dbPassword == null) {
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

        return new SimpleConnectionFactory(url, name, dbPassword);
    }

    private <T extends Enum<T> & IPropertiable> Map<T, String> loadSQLs(String name, Class<T> type) throws IOException, StorageInitializationException {
        Properties queries = new Properties();
        queries.load(getClass().getResourceAsStream(name));

        Map<T, String> qs = new EnumMap<T, String>(type);
        for (T q : type.getEnumConstants()) {
            String sql = (String) queries.remove(q.getPropertyName());
            if (sql != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Property '" + q.getPropertyName() + "' initialized with SQL: " + sql);
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
}
