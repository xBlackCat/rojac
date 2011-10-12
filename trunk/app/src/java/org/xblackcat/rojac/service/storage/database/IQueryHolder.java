package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;

import java.util.Collection;
import java.util.Map;

/**
 * The helper interface to avoid some duplicates in the code.
 */

interface IQueryHolder {
    int update(DataQuery sql, Object... params) throws StorageException;

    <T> T executeSingle(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException;

    <T> Collection<T> execute(IToObjectConverter<T> c, DataQuery sql, Object... params) throws StorageException;

    int[] getIds(DataQuery sql, Object... params) throws StorageException;

    @SuppressWarnings({"unchecked"})
    <K, O> Map<K, O> executeSingleBatch(IToObjectConverter<O> c, DataQuery sql, K... keys) throws StorageException;

    void updateBatch(DataQuery sql, Object[]... params) throws StorageException;
}
