package org.xblackcat.rojac.service.storage.importing;

import org.xblackcat.rojac.service.storage.StorageException;

/**
 * 11.10.11 11:21
 *
 * @author xBlackCat
 */
interface IRowHandler {
    boolean handleRow(Object[] row);

    void initialize(String[] columnNames) throws StorageException;
}
