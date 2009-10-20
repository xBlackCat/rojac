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
                Boolean c = helper.executeSingle(Converters.TO_BOOLEAN_CONVERTER, check.getSql());
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

    private Map<SQL, List<SQL>> loadInitializeSQLs(String checkProp, String initProps, String config) throws IOException {
        Properties check = ResourceUtils.loadProperties(checkProp);
        Properties init = ResourceUtils.loadProperties(initProps);
        Properties clue = ResourceUtils.loadProperties(config);

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
