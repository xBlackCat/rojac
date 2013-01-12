package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.service.storage.database.DBStorage;
import org.xblackcat.rojac.service.storage.database.DBStructureChecker;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.util.RojacUtils;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Helper class to simplify code to work with storage. Allowed to change storage in runtime.
 *
 * @author xBlackCat
 */
public final class Storage {
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

    public static IBatch startBatch() throws StorageException {
        try {
            lock.readLock().lock();
            return storage.startBatch();
        } finally {
            lock.readLock().unlock();
        }
    }

    static void setStorage(StorageSettings settings) throws StorageException {
        assert RojacUtils.checkThread(false);

        IStorage newStorage = initializeStorage(settings);
        IStorage oldStorage;

        lock.writeLock().lock();
        try {
            oldStorage = storage;
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

    private static IStorage initializeStorage(StorageSettings settings) throws StorageException {
        if (settings instanceof DatabaseSettings) {
            return new DBStorage((DatabaseSettings) settings);
        }

        throw new IllegalArgumentException("Unknown storage type: " + settings.getEngineName() + " (" + settings.getEngine() + ")");
    }

    public static IStructureChecker getChecker(StorageSettings settings) throws StorageInitializationException {
        if (settings instanceof DatabaseSettings) {
            return new DBStructureChecker((DatabaseSettings) settings);
        }

        throw new IllegalArgumentException("Unknown storage type: " + settings.getEngineName() + " (" + settings.getEngine() + ")");
    }
}
