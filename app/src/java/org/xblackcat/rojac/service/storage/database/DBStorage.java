package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.IProgressTracker;
import org.xblackcat.rojac.service.storage.*;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.service.storage.database.helper.IBatchedQueryHelper;
import org.xblackcat.rojac.service.storage.database.helper.IManagingQueryHelper;
import org.xblackcat.rojac.service.storage.database.helper.QueryHelperFactory;
import org.xblackcat.rojac.util.DatabaseUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ASUS
 */

public class DBStorage extends AQueryHolder<IManagingQueryHelper> implements IStorage {

    private final Map<Class<? extends AH>, AH> accessHelpers = new HashMap<>();
    private final Map<DataQuery, String> queries;


    public DBStorage(DatabaseSettings settings) throws StorageException {
        super(QueryHelperFactory.createHelper(settings));
        queries = DatabaseUtils.loadSQLs(helper.getEngine(), DataQuery.class);

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
        accessHelpers.put(IUtilAH.class, new DBUtilAH(this));
    }

    @Override
    public <T extends AH> T get(Class<T> base) {
        @SuppressWarnings({"unchecked"})
        T accessHelper = (T) accessHelpers.get(base);

        assert accessHelper != null : "Access helper is not registered: " + base.getSimpleName();

        return accessHelper;
    }

    @Override
    public void shutdown() throws StorageException {
        helper.shutdown();
    }

    @Override
    public IBatch startBatch() throws StorageException {
        return new DbBatch(helper.startBatch());
    }

    @Override
    protected String getQuery(DataQuery q) {
        return queries.get(q);
    }

    @Override
    public void updateBatch(
            DataQuery sql,
            IProgressTracker tracker,
            Collection<Object[]> params
    ) throws StorageException {
        helper.updateBatch(getQuery(sql), tracker, params);
    }

    private class DbBatch extends AQueryHolder<IBatchedQueryHelper> implements IBatch {
        private final Map<Class<? extends AH>, AH> batchAccessHelpers = new HashMap<>();

        public DbBatch(IBatchedQueryHelper queryHelper) {
            super(queryHelper);
        }

        @Override
        public <T extends AH> T get(Class<T> base) {
            @SuppressWarnings({"unchecked"})
            T accessHelper = (T) batchAccessHelpers.get(base);
            if (accessHelper != null) {
                return accessHelper;
            }

            //noinspection unchecked
            accessHelper = (T) accessHelpers.get(base);

            assert accessHelper != null : "Access helper is not registered: " + base.getSimpleName();

            try {
                Constructor<? extends AH> ahConstructor = accessHelper.getClass().getDeclaredConstructor(IQueryHolder.class);

                @SuppressWarnings({"unchecked"})
                T ah = (T) ahConstructor.newInstance(this);

                batchAccessHelpers.put(base, ah);

                return ah;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                assert false : "No constructor available for " + accessHelper.getClass();
                return null;
            }
        }

        @Override
        protected String getQuery(DataQuery q) {
            return DBStorage.this.getQuery(q);
        }

        @Override
        public void close() throws StorageException {
            try {
                helper.close();
            } catch (SQLException e) {
                throw new StorageException("Can't close batch connection", e);
            }
        }

        @Override
        public void updateBatch(
                DataQuery sql,
                IProgressTracker tracker,
                Collection<Object[]> params
        ) throws StorageException {
        }

        @Override
        public void commit() throws StorageException {
            helper.commit();
        }

        @Override
        public void rollback() throws StorageException {
            helper.rollback();
        }

    }
}
