package org.xblackcat.sunaj.service.storage.database.helper;

import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.IToObjectConvertor;

import java.util.Collection;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public interface IQueryHelper {
    <T> Collection<T> execute(IToObjectConvertor<T> c, String sql, Object... parameters) throws StorageException;

    <T> T executeSingle(IToObjectConvertor<T> c, String sql, Object... parameters) throws StorageException;

    void update(String sql, Object... parameters) throws StorageException;
}
