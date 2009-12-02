package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.rojac.service.storage.IModerateAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;
import ru.rsdn.Janus.JanusModerateInfo;

import java.util.Collection;

/**
 * @author ASUS
 */

final class DBModerateAH implements IModerateAH {
    private final IQueryExecutor helper;

    DBModerateAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeModerateInfo(JanusModerateInfo mi) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_MODERATE,
                mi.getMessageId(),
                mi.getUserId(),
                mi.getForumId(),
                mi.getCreate().getTimeInMillis());
    }

    public boolean removeModerateInfosByMessageId(int messageId) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECTS_MODERATE, messageId) > 0;
    }

    public Moderate[] getModeratesByMessageId(int messageId) throws StorageException {
        Collection<Moderate> m = helper.execute(Converters.TO_MODERATE, DataQuery.GET_OBJECTS_MODERATE_BY_MESSAGE_ID, messageId);
        return m.toArray(new Moderate[m.size()]);
    }

}
