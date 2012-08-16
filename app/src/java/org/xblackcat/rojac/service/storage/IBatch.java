package org.xblackcat.rojac.service.storage;

/**
 * 30.07.12 15:41
 *
 * @author xBlackCat
 */
public interface IBatch extends IAccessFactory, AutoCloseable {
    @Override
    void close() throws StorageException;

    void commit() throws StorageException;

    void rollback() throws StorageException;
}
