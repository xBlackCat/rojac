package org.xblackcat.rojac.service.storage.database;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.*;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.convert.Converters;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.service.storage.database.helper.IQueryHelper;
import org.xblackcat.rojac.service.storage.database.helper.QueryHelper;
import org.xblackcat.rojac.util.QueryUtils;

import java.io.IOException;
import java.util.Collection;
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
    private final DBFavoriteAH favoriteAH;

    public DBStorage(String propRoot, IConnectionFactory connectionFactory) throws StorageException {
        try {
            this.queries = QueryUtils.loadSQLs(propRoot, DataQuery.class);
            this.initializeQueries = QueryUtils.loadInitializeSQLs(propRoot);

            helper = new QueryHelper(connectionFactory);
        } catch (StorageInitializationException e) {
            throw e;
        } catch (IOException e) {
            throw new StorageInitializationException("Can not setup storage factory.", e);
        } catch (Exception e) {
            throw new StorageInitializationException("Unspecified exception occurs while DB storage initialization.", e);
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
        favoriteAH = new DBFavoriteAH(this);
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

    @Override
    public void shutdown() throws StorageException {
        helper.shutdown();
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
    public IFavoriteAH getFavoriteAH() {
        return favoriteAH;
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
