package org.xblackcat.rojac.service.storage.database;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.*;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.convert.Converters;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.service.storage.database.helper.IQueryHelper;
import org.xblackcat.rojac.service.storage.database.helper.QueryHelper;
import org.xblackcat.utils.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author ASUS
 */

public class DBStorage implements IStorage, IQueryExecutor {
    private static final Log log = LogFactory.getLog(DBStorage.class);

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

    public DBStorage(String propRoot, IConnectionFactory connectionFactory) throws StorageException {
        try {
            this.queries = loadSQLs('/' + propRoot + "/sql.data.properties", DataQuery.class);
            this.initializeQueries = loadInitializeSQLs(
                    '/' + propRoot + "/sql.check.properties",
                    '/' + propRoot + "/sql.initialize.properties",
                    '/' + propRoot + "/sql.depends.properties"
            );

            helper = new QueryHelper(connectionFactory);
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

    /* Initialization routines */
    @Override
    public void initialize() throws StorageException {
        if (log.isInfoEnabled()) {
            log.info("Check database storage structure started.");
        }
        for (Map.Entry<SQL, List<SQL>> entry : initializeQueries.entrySet()) {
            boolean success = false;
            SQL check = entry.getKey();
            if (log.isTraceEnabled()) {
                log.trace("Perform check " + check);
            }
            try {
                Boolean c = helper.executeSingle(Converters.TO_BOOLEAN, check.getSql());
                success = Boolean.TRUE.equals(c);
            } catch (StorageException e) {
                if (log.isTraceEnabled()) {
                    log.trace(check + " check failed. Reason: " + e.getLocalizedMessage());
                }
            }

            if (!success) {
                // If c is null or FALSE - abort.
                if (log.isTraceEnabled()) {
                    log.trace(check + " check failed. Perform initialization.");
                }

                for (SQL sql : entry.getValue()) {
                    try {
                        if (log.isTraceEnabled()) {
                            log.trace("Perform initialization command " + sql);
                        }
                        helper.update(sql.getSql());
                    } catch (StorageException e) {
                        log.error("Can not perform initialization procedure " + sql);
                        throw new StorageInitializationException("Can not execute " + sql, e);
                    }
                }

                // Perform post-check
                try {
                    if (log.isTraceEnabled()) {
                        log.trace("Perform post-initialization check " + check);
                    }
                    Boolean c = helper.executeSingle(Converters.TO_BOOLEAN, check.getSql());
                    success = Boolean.TRUE.equals(c);
                } catch (StorageException e) {
                    throw new StorageCheckException("Post check failed for " + check, e);
                }
            }
        }
    }

    @Override
    public IForumAH getForumAH() {
        return forumAH;
    }

    @Override
    public IForumGroupAH getForumGroupAH() {
        return forumGroupAH;
    }

    @Override
    public IMessageAH getMessageAH() {
        return messageAH;
    }

    @Override
    public IModerateAH getModerateAH() {
        return moderateAH;
    }

    @Override
    public INewMessageAH getNewMessageAH() {
        return newMessageAH;
    }

    @Override
    public INewModerateAH getNewModerateAH() {
        return newModerateAH;
    }

    @Override
    public INewRatingAH getNewRatingAH() {
        return newRatingAH;
    }

    @Override
    public IRatingAH getRatingAH() {
        return ratingAH;
    }

    @Override
    public IUserAH getUserAH() {
        return userAH;
    }

    @Override
    public IVersionAH getVersionAH() {
        return versionAH;
    }

    @Override
    public IMiscAH getMiscAH() {
        return miscAH;
    }

    @Override
    public int update(DataQuery sql, Object... params) throws StorageException {
        return helper.update(getQuery(sql), params);
    }

    @Override
    public <T> T executeSingle(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException {
        return helper.executeSingle(c, getQuery(sql), params);
    }

    @Override
    public <K, O> Map<K, O> executeSingleBatch(IToObjectConverter<O> c, DataQuery sql, K[] keys) throws StorageException {
        return helper.executeSingleBatch(c, getQuery(sql), keys);
    }

    @Override
    public <T> Collection<T> execute(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException {
        return helper.execute(c, getQuery(sql), params);
    }

    @Override
    public int[] getIds(DataQuery sql, Object... params) throws StorageException {
        Collection<Integer> objIds = execute(Converters.TO_INTEGER, sql, params);
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

    private <T extends Enum<T> & IPropertiable> Map<T, String> loadSQLs(String name, Class<T> type) throws IOException, StorageInitializationException {
        Properties queries = ResourceUtils.loadProperties(name);

        Map<T, String> qs = new EnumMap<T, String>(type);
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
     * Loads and returns a new Properties object for given resource or path name.
     *
     * @param propertiesFile
     *
     * @return
     */
    private static Map<String, String> loadProperties(String propertiesFile) throws IOException {
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

        final Map<String, String> map = new LinkedHashMap<String, String>();
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
    private Map<SQL, List<SQL>> loadInitializeSQLs(String checkProp, String initProps, String config) throws IOException {
        Map<String, String> check = loadProperties(checkProp);
        Properties init = ResourceUtils.loadProperties(initProps);
        Properties clue = ResourceUtils.loadProperties(config);

        Map<SQL, List<SQL>> map = new LinkedHashMap<SQL, List<SQL>>();

        for (Map.Entry<String, String> ce : check.entrySet()) {
            String name = ce.getKey();
            String sql = ce.getValue();

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
            return getName();
        }
    }
}
