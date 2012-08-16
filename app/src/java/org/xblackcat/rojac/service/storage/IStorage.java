package org.xblackcat.rojac.service.storage;

/**
 * @author ASUS
 */

public interface IStorage extends IAccessFactory {
    void shutdown() throws StorageException;

    IBatch startBatch() throws StorageException;
}
