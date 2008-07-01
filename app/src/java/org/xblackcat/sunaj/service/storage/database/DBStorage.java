package org.xblackcat.sunaj.service.storage.database;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.service.storage.*;
import org.xblackcat.sunaj.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.sunaj.service.storage.database.connection.SimpleConnectionFactory;
import org.xblackcat.sunaj.service.storage.database.convert.Converters;
import org.xblackcat.sunaj.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.sunaj.service.storage.database.helper.IQueryHelper;
import org.xblackcat.sunaj.service.storage.database.helper.QueryHelper;
import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.util.*;

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
    private final Map<SQL, List<SQL>> initializeQueries;

    private final IQueryHelper helper;

    private final DBForumAH forumAH;
    private final DBRatingAH ratingAH;
    private final DBUserAH userAH;
    private final DBForumGroupAH forumGroupAH;
    private final DBVersionAH versionAH;
    private final DBNewRatingAH newRatingAH;
    private final DBModerateAH moderateAH;
    private final DBNewModerateAH newModerateAH;
    private final DBNewMessageAH newMessageAH;
    private final DBMessageAH messageAH;
    private final DBMiscAH miscAH;

    public DBStorage(String propRoot) throws StorageException {
        try {
            IConnectionFactory cf = initializeConnectionFactory('/' + propRoot + "/database.properties");

            this.queries = loadSQLs('/' + propRoot + "/sql.data.properties", DataQuery.class);
            this.initializeQueries = loadInitializeSQLs('/' + propRoot + "/sql.check.properties", '/' + propRoot + "/sql.initialize.properties", '/' + propRoot + "/sql.depends.properties");

            helper = new QueryHelper(cf);
        } catch (StorageInitializationException e) {
            throw e;
        } catch (IOException e) {
            throw new StorageInitializationException("Can not setup storage factory.", e);
        } catch (Exception e) {
            throw new StorageInitializationException("Unspecified exception occurs while DB storage initializating.", e);
        }

        // Initialize the object AHs
        forumAH = new DBForumAH(this);
        ratingAH = new DBRatingAH(this);
        userAH = new DBUserAH(this);
        forumGroupAH = new DBForumGroupAH(this);
        versionAH = new DBVersionAH(this);
        newRatingAH = new DBNewRatingAH(this);
        moderateAH = new DBModerateAH(this);
        newMessageAH = new DBNewMessageAH(this);
        messageAH = new DBMessageAH(this);
        miscAH = new DBMiscAH(this);
        newModerateAH = new DBNewModerateAH(this);
    }

    private Map<SQL, List<SQL>> loadInitializeSQLs(String checkProp, String initProps, String config) throws IOException {
        Properties check = new Properties();
        Properties init = new Properties();
        Properties clue = new Properties();
        check.load(ResourceUtils.getResourceAsStream(checkProp));
        init.load(ResourceUtils.getResourceAsStream(initProps));
        clue.load(ResourceUtils.getResourceAsStream(config));

        Map<SQL, List<SQL>> map = new HashMap<SQL, List<SQL>>();

        for (Map.Entry<Object, Object> ce : check.entrySet()) {
            String name = (String) ce.getKey();
            String sql = (String) ce.getValue();

            String inits = clue.getProperty(name, "");
            List<SQL> sqls = new ArrayList<SQL>();
            String[] initNames = inits.trim().split("\\s+,\\s+");
            if (!ArrayUtils.isEmpty(initNames)) {
                for (String initName : initNames) {
                    String initSql = init.getProperty(initName);
                    if (StringUtils.isBlank(initSql)) {
                        if (log.isWarnEnabled()) {
                            log.warn(initName + " SQL not defined (Used in " + name + "). Initialization routine can be work improperly.");
                        }
                    } else {
                        sqls.add(new SQL(initName, initSql));
                    }
                }
            }

            map.put(new SQL(name, sql), sqls);
        }

        return Collections.unmodifiableMap(map);
    }

    /* Initialization routines */
    public void initialize() throws StorageException {
        if (log.isInfoEnabled()) {
            log.info("Check database storage structure started.");
        }
        for (Map.Entry<SQL,List<SQL>> entry : initializeQueries.entrySet()) {
            boolean success = false;
            SQL check = entry.getKey();
            if (log.isDebugEnabled()) {
                log.debug("Perform check " + check);
            }
            try {
                Boolean c = helper.executeSingle(Converters.TO_BOOLEAN_CONVERTER, check.getSql());
                success = Boolean.TRUE.equals(c);
            } catch (StorageException e) {
                if (log.isTraceEnabled()) {
                    log.trace(check + " check failed.", e);
                }
            }

            if (!success) {
                // If c is null or FALSE - abort.
                if (log.isDebugEnabled()) {
                    log.debug(check + " check failed. Perform initialization.");
                }

                for (SQL sql : entry.getValue()) {
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Perform initialization command " + sql);
                        }
                        helper.update(sql.getSql());
                    } catch (StorageException e) {
                        log.error("Can not perform initialization procedure " + sql);
                        throw new StorageInitializationException("Can not execute " + sql, e);
                    }
                }

                // Perform post-check
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Perform post-initialization check " + check);
                    }
                    Boolean c = helper.executeSingle(Converters.TO_BOOLEAN_CONVERTER, check.getSql());
                    success = Boolean.TRUE.equals(c);
                } catch (StorageException e) {
                    throw new StorageCheckException("Post check failed for " + check, e);
                }
            }
        }
    }

    public IForumAH getForumAH() {
        return forumAH;
    }

    public IForumGroupAH getForumGroupAH() {
        return forumGroupAH;
    }

    public IMessageAH getMessageAH() {
        return messageAH;
    }

    public IModerateAH getModerateAH() {
        return moderateAH;
    }

    public INewMessageAH getNewMessageAH() {
        return newMessageAH;
    }

    public INewModerateAH getNewModerateAH() {
        return newModerateAH;
    }

    public INewRatingAH getNewRatingAH() {
        return newRatingAH;
    }

    public IRatingAH getRatingAH() {
        return ratingAH;
    }

    public IUserAH getUserAH() {
        return userAH;
    }

    public IVersionAH getVersionAH() {
        return versionAH;
    }

    public IMiscAH getMiscAH() {
        return miscAH;
    }

    public int update(DataQuery sql, Object... params) throws StorageException {
        return helper.update(getQuery(sql), params);
    }

    public <T> T executeSingle(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException {
        return helper.executeSingle(c, getQuery(sql), params);
    }

    public <T> Collection<T> execute(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException {
        return helper.execute(c, getQuery(sql), params);
    }

    public int[] getIds(DataQuery sql, Object... params) throws StorageException {
        Collection<Integer> objIds = execute(Converters.TO_INTEGER_CONVERTER, sql, params);
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

    private static class SQL {
        private final String name;
        private final String sql;

        private SQL(String name, String sql) {
            this.name = name;
            this.sql = sql;
        }

        public String getName() {
            return name;
        }

        public String getSql() {
            return sql;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
