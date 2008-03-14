package org.xblackcat.sunaj.service.storage.database.helper;

import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.IToObjectConverter;

import java.util.Collection;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public interface IQueryHelper {
    <T> Collection<T> execute(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException;

    <T> T executeSingle(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException;

    int update(String sql, Object... parameters) throws StorageException;
}
