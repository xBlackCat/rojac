/**
 * Date: 15.04.2007
 * @author ASUS
 */

package org.xblackcat.sunaj.service.storage.filesystem;

import org.xblackcat.sunaj.service.storage.IStorage;
import org.xblackcat.sunaj.service.storage.IStorageFactory;
import org.xblackcat.sunaj.service.storage.StorageCheckException;
import org.xblackcat.sunaj.service.storage.StorageException;

import java.io.File;

public final class FileSystemStorageFactory implements IStorageFactory {
    private final FileSystemStorage storage;

    public FileSystemStorageFactory(String rootFolder) throws StorageException {
        File root = new File(rootFolder);
        if (!root.exists()) {
            throw new StorageCheckException("The path '" + rootFolder + " is not exists.");
        }
        if (!root.isDirectory()) {
            throw new StorageCheckException("The path '" + rootFolder + " is not a folder.");
        }

        storage = new FileSystemStorage(root);
    }

    public IStorage getStorage() {
        return storage;
    }
}
