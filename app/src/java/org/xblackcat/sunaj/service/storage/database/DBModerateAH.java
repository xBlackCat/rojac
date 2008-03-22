package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.data.Moderate;
import org.xblackcat.sunaj.service.storage.IModerateAH;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.Converters;

import java.util.Collection;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

final class DBModerateAH implements IModerateAH {
    private final IQueryExecutor helper;

    DBModerateAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeModerateInfo(Moderate mi) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_MODERATE,
                mi.getMessageId(),
                mi.getUserId(),
                mi.getForumId(),
                mi.getCreationTime());
    }

    public boolean removeModerateInfosByMessageId(int messageId) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECTS_MODERATE, messageId) > 0;
    }

    public Moderate[] getModeratesByMessageId(int messageId) throws StorageException {
        Collection<Moderate> m = helper.execute(Converters.TO_MODERATE_CONVERTER, DataQuery.GET_OBJECTS_MODERATE_BY_MESSAGE_ID, messageId);
        return m.toArray(new Moderate[m.size()]);
    }

}
