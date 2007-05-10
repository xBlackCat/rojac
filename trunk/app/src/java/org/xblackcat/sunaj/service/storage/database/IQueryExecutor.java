package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.IToObjectConvertor;

import java.util.Collection;

/**
 * The helper interface to avoid some duplicates in the code.
 */

interface IQueryExecutor {
    int update(DataQuery sql, Object... params) throws StorageException;

    <T> T executeSingle(IToObjectConvertor<T> c, DataQuery sql, Object... params) throws StorageException;

    <T> Collection<T> execute(IToObjectConvertor<T> c, DataQuery sql, Object... params) throws StorageException;
}
