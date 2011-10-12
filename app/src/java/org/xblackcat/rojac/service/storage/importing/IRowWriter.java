package org.xblackcat.rojac.service.storage.importing;

import org.xblackcat.rojac.service.storage.StorageException;

/**
 * 11.10.11 15:12
 *
 * @author xBlackCat
 */
public interface IRowWriter {
    int storeRow(Cell[] cells) throws StorageException;

    void stop() throws StorageException;
}