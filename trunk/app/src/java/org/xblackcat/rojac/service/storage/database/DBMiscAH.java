package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * @author ASUS
 */

final class DBMiscAH extends AnAH implements IMiscAH {
    DBMiscAH(IQueryHolder helper) {
        super(helper);
    }

    public void storeExtraMessage(int messageId) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_EXTRA_MESSAGE, messageId);
    }

    public void removeExtraMessage(int messageId) throws StorageException {
        helper.update(DataQuery.REMOVE_OBJECT_EXTRA_MESSAGE, messageId);
    }

    @Override
    public void addToIgnoredTopicList(int topicId) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_IGNORED_TOPIC, topicId);
    }

    @Override
    public void removeFromIgnoredTopicList(int topicId) throws StorageException {
        helper.update(DataQuery.REMOVE_OBJECT_IGNORED_TOPIC, topicId);
    }

    public void clearExtraMessages() throws StorageException {
        helper.update(DataQuery.REMOVE_ALL_OBJECTS_EXTRA_MESSAGE);
    }

    public int[] getExtraMessages() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_EXTRA_MESSAGE);
    }
}
