package org.xblackcat.sunaj.service.storage.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.service.storage.*;
import org.xblackcat.sunaj.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.sunaj.service.storage.database.connection.SimpleConnectionFactory;
import org.xblackcat.sunaj.service.storage.database.convert.ToBooleanConvertor;
import org.xblackcat.sunaj.service.storage.database.helper.IQueryHelper;
import org.xblackcat.sunaj.service.storage.database.helper.QueryHelper;

import java.io.IOException;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Properties;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public class DBStorage implements IStorage {
    private static final Log log = LogFactory.getLog(DBStorage.class);

    private static final String DSSTORAGE_JDBC_CLASS = "db.jdbc.driver.class";
    private static final String DSSTORAGE_URL = "db.connection.url";
    private static final String DSSTORAGE_USER = "db.access.user";
    private static final String DSSTORAGE_PASSWORD = "db.access.password";

    private final Map<DataQuery, String> queries;
    private final Map<CheckQuery, String> checkQueries;
    private final Map<InitializeQuery, String> initializeQueries;

    private final IQueryHelper helper;

    public DBStorage(String propRoot) throws StorageException {
        try {
            IConnectionFactory cf = initializeConnectionFactory('/' + propRoot + "/database.properties");

            this.queries = loadSQLs('/' + propRoot + "/sql.properties", DataQuery.class);
            this.checkQueries = loadSQLs('/' + propRoot + "/check.properties", CheckQuery.class);
            this.initializeQueries = loadSQLs('/' + propRoot + "/initialize.properties", InitializeQuery.class);

            helper = new QueryHelper(cf);
        } catch (IOException e) {
            throw new StorageException("Can not setup storage factory.", e);
        } catch (ClassNotFoundException e) {
            throw new StorageInitializationException("Can not initialize JDBC driver.", e);
        } catch (IllegalAccessException e) {
            throw new StorageInitializationException("Can not initialize JDBC driver.", e);
        } catch (InstantiationException e) {
            throw new StorageInitializationException("Can not initialize JDBC driver.", e);
        } catch (Exception e) {
            throw new StorageException("Exception occurs while DB storage initializating.", e);
        }
    }

    /* Initialization routines */
    public boolean checkStructure() throws StorageException {
        if (log.isInfoEnabled()) {
            log.info("Check database storage structure started.");
        }
        for (Map.Entry<CheckQuery, String> e : checkQueries.entrySet()) {
            if (log.isDebugEnabled()) {
                log.debug("Checking: " + e.getKey());
            }
            Boolean c = helper.executeSingle(new ToBooleanConvertor(), e.getValue());
            if (!Boolean.TRUE.equals(c)) {
                // If c is null or FALSE - abort.
                if (log.isDebugEnabled()) {
                    log.debug(e.getKey() + " check failed.");
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
        for (Map.Entry<InitializeQuery,String> e : initializeQueries.entrySet()) {
            if (log.isDebugEnabled()) {
                log.debug("Performing: " + e.getKey());
            }
            helper.update(e.getValue());
        }
    }

    public IForumDAO getForumDAO() {
        return null;
    }

    public IForumGroupDAO getForumGroupDAO() {
        return null;
    }

    public IMessageDAO getMessageDAO() {
        return null;
    }

    public IModerateDAO getModerateDAO() {
        return null;
    }

    public INewMessageDAO getNewMessageDAO() {
        return null;
    }

    public INewRatingDAO getNewRatingDAO() {
        return null;
    }

    public IRatingDAO getRatingDAO() {
        return null;
    }

    public IUserDAO getUserDAO() {
        return null;
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

    private IConnectionFactory initializeConnectionFactory(String name) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Loading database connection properties.");
        }
        // The properties file should be located in /<propRoot>/database.properties
        Properties databaseProperties = new Properties();
        databaseProperties.load(getClass().getResourceAsStream(name));

        String jdbcClass = databaseProperties.getProperty(DSSTORAGE_JDBC_CLASS);
        if (jdbcClass == null) {
            throw new StorageInitializationException("The " + DSSTORAGE_JDBC_CLASS + " property is not defined.");
        }

        Class.forName(jdbcClass).newInstance();

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

        return new SimpleConnectionFactory(url, name, dbPassword);
    }

    private <T extends Enum<T> & IPropertiable> Map<T, String> loadSQLs(String name, Class<T> type) throws IOException {
        Properties queries = new Properties();
        queries.load(getClass().getResourceAsStream(name));

        Map<T, String> qs = new EnumMap<T, String>(type);
        for (T q : type.getEnumConstants()) {
            String sql = (String) queries.remove(q.getPropertyName());
            if (log.isDebugEnabled()) {
                log.debug("Property '" + q.getPropertyName() + "' initialized with SQL: " + sql);
            }
            qs.put(q, sql);
        }

        if (!queries.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("There are unused properties exists in " + name);
                for (Map.Entry<Object,Object> entry : queries.entrySet()) {
                    log.warn("Property: " + entry.getKey() + " = " + entry.getValue());
                }
            }
        }

        return Collections.unmodifiableMap(qs);
    }
}
