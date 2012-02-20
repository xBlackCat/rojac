package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;

import java.util.List;

/**
 * @author ASUS
 */

public interface IQueryHelper {
    <T> List<T> execute(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException;

    <T> T executeSingle(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException;

    int update(String sql, Object... parameters) throws StorageException;

    void shutdown();

    String getEngine();

    void updateBatch(String query, Object[]... params) throws StorageException;
}
