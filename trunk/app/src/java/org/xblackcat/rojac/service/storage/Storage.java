package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.service.storage.database.DBStorage;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.util.RojacUtils;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Helper class to simplify code to work with storage. Allowed to change storage in runtime.
 *
 * @author xBlackCat
 */
public class Storage {
    private static IStorage storage;

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public static <T extends AH> T get(Class<T> ahClass) {
        lock.readLock().lock();
        try {
            return storage.get(ahClass);
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void setStorage(DatabaseSettings settings) throws StorageException {
        assert RojacUtils.checkThread(false);

        if (settings == null) {
            throw new NullPointerException("Invalid settings.");
        }

        DBStorage newStorage = new DBStorage(settings);
        IStorage oldStorage = storage;

        lock.writeLock().lock();
        try {
            newStorage.check();
            storage = newStorage;
        } finally {
            lock.writeLock().unlock();
        }

        if (oldStorage != null) {
            oldStorage.shutdown();
        }
    }

    public static void shutdown() throws StorageException {
        lock.writeLock().lock();
        try {
            if (storage != null) {
                storage.shutdown();
            }
        } finally {
            lock.writeLock().unlock();
        }

    }
}
