package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.IProgressTracker;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;

import java.util.Collection;

/**
 * The helper interface to avoid some duplicates in the code.
 */

interface IQueryHolder {
    int update(DataQuery sql, Object... params) throws StorageException;

    <T> T executeSingle(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException;

    <T> org.xblackcat.rojac.service.storage.IResult<T> execute(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException;

    int[] getIds(DataQuery sql, Object... params) throws StorageException;

    void updateBatch(DataQuery sql, IProgressTracker tracker, Collection<Object[]> params) throws StorageException;
}
