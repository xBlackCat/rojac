package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Date: 15 трав 2007
 *
 * @author ASUS
 */

final class DBMiscAH implements IMiscAH {
    private final IQueryExecutor helper;

    DBMiscAH(IQueryExecutor helper) {
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
