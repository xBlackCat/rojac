package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.StorageException;

/**
 * 11.10.11 11:21
 *
 * @author xBlackCat
 */
public interface IRowHandler {
    boolean handleRow(Object[] row);

    void initialize(String[] columnNames) throws StorageException;
}
