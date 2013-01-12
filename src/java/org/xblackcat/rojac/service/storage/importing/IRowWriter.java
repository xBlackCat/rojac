package org.xblackcat.rojac.service.storage.importing;

import org.xblackcat.rojac.service.storage.StorageException;

/**
 * 11.10.11 15:12
 *
 * @author xBlackCat
 */
interface IRowWriter extends AutoCloseable {
    void initialize(String[] columnNames) throws StorageException;

    int storeRow(Object[] cells) throws StorageException;

    void close() throws StorageException;
}
