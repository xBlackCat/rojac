package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Cell;
import org.xblackcat.rojac.service.storage.database.IRowHandler;

import java.util.Collection;

/**
 * 05.10.11 9:55
 *
 * @author xBlackCat
 */
public interface IImportHelper {
    Collection<String> getItemsList() throws StorageException;
    
    void getData(IRowHandler rowHandler, String item) throws StorageException;

    int storeItem(String item, Cell[] data) throws StorageException;

    int getRows(String item) throws StorageException;
}
