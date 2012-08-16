package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;

/**
 * 14.08.12 16:15
 *
 * @author xBlackCat
 */
public interface IQueryHelper {
    <T> IResult<T> execute(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException;

    <T> T executeSingle(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException;

    int update(String sql, Object... parameters) throws StorageException;
}
