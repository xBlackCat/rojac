package org.xblackcat.rojac.service.storage.importing;

import org.xblackcat.rojac.service.storage.StorageException;

import java.util.Collection;

/**
 * 05.10.11 9:55
 *
 * @author xBlackCat
 */
interface IImportHelper {
    Collection<String> getItemsList() throws StorageException;

    void getData(IRowHandler rowHandler, String item) throws StorageException;

    IRowWriter getRowWriter(String item, boolean merge) throws StorageException;

    int getRows(String item) throws StorageException;
}
