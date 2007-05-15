package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.service.storage.IMiscDAO;
import org.xblackcat.sunaj.service.storage.StorageException;

/**
 * Date: 15 трав 2007
 *
 * @author ASUS
 */

final class DBMiscDAO implements IMiscDAO {
    private final IQueryExecutor helper;

    DBMiscDAO(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeExtraMessage(int messageId) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_EXTRA_MESSAGE, messageId);
    }

    public void removeExtraMessage(int messageId) throws StorageException {
        helper.update(DataQuery.REMOVE_OBJECT_EXTRA_MESSAGE, messageId);
    }

    public void clearExtraMessages() throws StorageException {
        helper.update(DataQuery.REMOVE_ALL_OBJECTS_EXTRA_MESSAGE);
    }

    public int[] getExtraMessages() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_EXTRA_MESSAGE);
    }
}
