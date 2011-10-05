package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Cell;

import java.util.Collection;

/**
 * 05.10.11 9:55
 *
 * @author xBlackCat
 */
public interface IImportHelper {
    Collection<String> getItemsList() throws StorageException;
    
    Iterable<Cell[]> getData(String item) throws StorageException;

    int storeItem(String item, Cell[] data) throws StorageException;
}
