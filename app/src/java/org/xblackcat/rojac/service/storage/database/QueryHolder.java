package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.StorageDataException;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.service.storage.database.helper.IQueryHelper;
import org.xblackcat.rojac.util.DatabaseUtils;

import java.util.Collection;
import java.util.Map;

/**
 * 27.09.11 16:17
 *
 * @author xBlackCat
 */
class QueryHolder implements IQueryHolder {
    private final Map<DataQuery, String> queries;
    private final IQueryHelper helper;

    public QueryHolder(IQueryHelper helper) throws StorageInitializationException {
        this.helper = helper;
        queries = DatabaseUtils.loadSQLs(helper.getEngine(), DataQuery.class);
    }

    protected String getQuery(DataQuery q) {
        return queries.get(q);
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
    public void updateBatch(DataQuery sql, Object[]... params) throws StorageException {
        helper.updateBatch(getQuery(sql), params);
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
}
