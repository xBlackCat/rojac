package org.xblackcat.sunaj.service.storage.filesystem;

import org.xblackcat.sunaj.service.storage.StorageCheckException;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.StorageInitializationException;
import org.xblackcat.sunaj.service.storage.StorageParseException;
import org.xblackcat.sunaj.service.storage.StorageWriteException;
import org.xblackcat.sunaj.service.storage.filesystem.parsers.IObjectParser;
import org.xblackcat.sunaj.service.storage.filesystem.writers.IObjectWriter;

import java.io.File;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

abstract class AbstractFileSystemDAO {
    private final File root;
    protected final File store;

    protected AbstractFileSystemDAO(File root, String storageName) {
        this.root = root;
        store = new File(root, storageName);
    }

    boolean checkStructure() throws StorageException {
        return store.isDirectory();
    }

    void initialize() throws StorageException {
        if (!root.exists()) {
            throw new StorageCheckException("The path '" + root.getPath() + " is not exists.");
        }
        if (!root.isDirectory()) {
            throw new StorageCheckException("The path '" + root.getPath() + " is not a folder.");
        }

        try {
            if (!store.exists()) {
                store.mkdirs();
            }

            if (!store.isDirectory()) {
                throw new StorageInitializationException("A file with name " + store.getName() + " already exists in " + root.getPath());
            }

        } catch (SecurityException e) {
            throw new StorageException("Security manager deny actions.", e);
        }
    }

    protected <T> T getRecord(int recordId, IObjectParser<T> p) throws StorageParseException {
        String keyPattern = getPrimaryKeySearchPattern(recordId);
        String[] rfn = searchFilesByPattern(keyPattern);
        if (rfn == null || rfn.length == 0) {
            return null;
        } else {
            if (rfn.length > 1) {
                throw new RuntimeException("Got " + rfn.length + " records for pattern '" + keyPattern + "'.");
            }
            return p.parseObject(new File(store, rfn[0]));
        }
    }

    protected <T> void saveRecord(T o, IObjectWriter<T> w) throws StorageWriteException {
        w.writeObject(store, o);
    }

    /**
     * Searchs and returns the records file name by pattern (regex) or <code>null</code> if record is not exists.
     *
     * @param pattern regex pattern of the file record.
     *
     * @return file name of the record or <code>null</code>.
     */
    protected String[] searchFilesByPattern(String pattern) {
        return store.list(new PatternFilenameFilter(pattern));
    }

    protected int[] getPrimaryKeys(String pattern) {
        String[] names = searchFilesByPattern(pattern);
        int[] keys = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            keys[i] = extractPrimaryKey(names[i]);
        }
        return keys;
    }

    /**
     * Returns the pattern for searching file record by primary key.
     *
     * @return the pattern with primary key.
     */

    protected int[] getAllIds() {
        return getPrimaryKeys(".*");
    }

    protected boolean removeRecord(int id) throws StorageException {
        String keyPattern = getPrimaryKeySearchPattern(id);
        String[] rfn = searchFilesByPattern(keyPattern);
        if (rfn == null || rfn.length == 0) {
            // Record already removed.
            return false;
        } else {
            if (rfn.length > 1) {
                throw new RuntimeException("Got " + rfn.length + " records for pattern '" + keyPattern + "'.");
            }
            try {
                return new File(store, rfn[0]).delete();
            } catch (SecurityException e) {
                throw new StorageException("Removing record fail.", e);
            }
        }
    }

    protected abstract int extractPrimaryKey(String recordFileName);

    protected abstract String getPrimaryKeySearchPattern(int recordId);
}
