package org.xblackcat.rojac.service.storage.database;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageDataException;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.service.storage.database.helper.IQueryHelper;

/**
 * 14.08.12 16:10
 *
 * @author xBlackCat
 */
public abstract class AQueryHolder<I extends IQueryHelper> implements IQueryHolder {
    protected final I helper;

    public AQueryHolder(I queryHelper) {
        helper = queryHelper;
    }

    protected abstract String getQuery(DataQuery q);

    @Override
    public int update(DataQuery sql, Object... params) throws StorageException {
        return helper.update(getQuery(sql), params);
    }

    @Override
    public <T> T executeSingle(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException {
        return helper.executeSingle(c, getQuery(sql), params);
    }

    @Override
    public <T> IResult<T> execute(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException {
        return helper.execute(c, getQuery(sql), params);
    }

    @Override
    public int[] getIds(DataQuery sql, Object... params) throws StorageException {
        try (IResult<Number> objIds = execute(Converters.TO_NUMBER, sql, params)) {
            TIntList ids = new TIntArrayList();

            try {
                for (Number id : objIds) {
                    if (id != null) { // Skip null values
                        ids.add(id.intValue());
                    }
                }
            } catch (NullPointerException e) {
                throw new StorageDataException("Got null instead of real value.", e);
            }

            return ids.toArray();
        }
    }
}
