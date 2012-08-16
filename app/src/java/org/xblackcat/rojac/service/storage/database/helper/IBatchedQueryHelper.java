package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.storage.StorageException;

import java.sql.SQLException;

/**
 * 30.07.12 15:50
 *
 * @author xBlackCat
 */
public interface IBatchedQueryHelper extends IQueryHelper, AutoCloseable {
    void commit() throws StorageException;

    void rollback() throws StorageException;

    @Override
    void close() throws SQLException;
}
