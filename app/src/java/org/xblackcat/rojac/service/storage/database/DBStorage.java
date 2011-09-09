package org.xblackcat.rojac.service.storage.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.*;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.convert.Converters;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.service.storage.database.helper.IQueryHelper;
import org.xblackcat.rojac.service.storage.database.helper.QueryHelper;
import org.xblackcat.rojac.util.DatabaseUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ASUS
 */

public class DBStorage implements IStorage, IQueryExecutor {
    private static final Log log = LogFactory.getLog(DBStorage.class);

    private final Map<DataQuery, String> queries;
    private final Map<SQL, List<SQL>> initializeQueries;

    private final IQueryHelper helper;

    private final Map<Class<? extends AH>, AH> accessHelpers = new HashMap<>();

    public DBStorage(String engine, IConnectionFactory connectionFactory) throws StorageException {
        try {
            this.queries = DatabaseUtils.loadSQLs(engine, DataQuery.class);
            this.initializeQueries = DatabaseUtils.loadInitializeSQLs(engine);

            helper = new QueryHelper(connectionFactory);
        } catch (StorageInitializationException e) {
            throw e;
        } catch (IOException e) {
            throw new StorageInitializationException("Can not setup storage factory.", e);
        } catch (Exception e) {
            throw new StorageInitializationException("Unspecified exception occurs while DB storage initialization.", e);
        }

        // Initialize the object AHs
        accessHelpers.put(IForumAH.class, new DBForumAH(this));
        accessHelpers.put(IRatingAH.class, new DBRatingAH(this));
        accessHelpers.put(IUserAH.class, new DBUserAH(this));
        accessHelpers.put(IForumGroupAH.class, new DBForumGroupAH(this));
        accessHelpers.put(IVersionAH.class, new DBVersionAH(this));
        accessHelpers.put(INewRatingAH.class, new DBNewRatingAH(this));
        accessHelpers.put(IModerateAH.class, new DBModerateAH(this));
        accessHelpers.put(INewMessageAH.class, new DBNewMessageAH(this));
        accessHelpers.put(IMessageAH.class, new DBMessageAH(this));
        accessHelpers.put(IMiscAH.class, new DBMiscAH(this));
        accessHelpers.put(INewModerateAH.class, new DBNewModerateAH(this));
        accessHelpers.put(IFavoriteAH.class, new DBFavoriteAH(this));
        accessHelpers.put(IStatisticAH.class, new DBStatisticAH(this));
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

                if (!success) {
                    throw new StorageCheckException("Post check failed for " + check);
                }
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public <T extends AH> T get(Class<T> base) {
        return (T) accessHelpers.get(base);
    }

    @Override
    public void shutdown() throws StorageException {
        helper.shutdown();
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
    @SafeVarargs
    public final <K, O> Map<K, O> executeSingleBatch(IToObjectConverter<O> c, DataQuery sql, K... keys) throws StorageException {
        return helper.executeSingleBatch(c, getQuery(sql), keys);
    }

    @Override
    public <T> Collection<T> execute(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException {
        return helper.execute(c, getQuery(sql), params);
    }

    @Override
    public int[] getIds(DataQuery sql, Object... params) throws StorageException {
        Collection<Number> objIds = execute(Converters.TO_NUMBER, sql, params);
        int[] ids;

        try {
            ids = new int[objIds.size()];

            int i = 0;
            for (Number id : objIds) {
                ids[i++] = id.intValue();
            }
        } catch (NullPointerException e) {
            throw new StorageDataException("Got null instead of real value.", e);
        }

        return ids;
    }

    protected String getQuery(DataQuery q) {
        return queries.get(q);
    }

}
