package org.xblackcat.rojac.service.storage.database;

import gnu.trove.list.array.TIntArrayList;
import org.xblackcat.rojac.service.IProgressTracker;
import org.xblackcat.rojac.service.storage.IResult;
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
    public void updateBatch(DataQuery sql, IProgressTracker tracker, Collection<Object[]> params) throws StorageException {
        helper.updateBatch(getQuery(sql), tracker, params);
    }

    @Override
    public <T> IResult<T> execute(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException {
        return helper.execute(c, getQuery(sql), params);
    }

    @Override
    public int[] getIds(DataQuery sql, Object... params) throws StorageException {
        Iterable<Number> objIds = execute(Converters.TO_NUMBER, sql, params);
        TIntArrayList ids = new TIntArrayList();

        try {
            for (Number id : objIds) {
                ids.add(id.intValue());
            }
        } catch (NullPointerException e) {
            throw new StorageDataException("Got null instead of real value.", e);
        }

        return ids.toArray();
    }
}
