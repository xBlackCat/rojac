package org.xblackcat.sunaj.service.storage.filesystem.writers;

import org.xblackcat.sunaj.service.storage.StorageWriteException;

import java.io.File;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IObjectWriter<T> {
    /**
     * Creates a record in the storage and returns the name of the record file.
     *
     * @param root
     * @param o @return
     */
    String writeObject(File root, T o) throws StorageWriteException;
}
