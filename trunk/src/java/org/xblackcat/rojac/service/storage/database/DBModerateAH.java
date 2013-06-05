package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Moderate;
import org.xblackcat.rojac.service.storage.IModerateAH;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
 * @author ASUS
 */

final class DBModerateAH extends AnAH implements IModerateAH {
    DBModerateAH(IQueryHolder helper) {
        super(helper);
    }

    public void storeModerateInfo(int messageId, int userId, int forumId, long createDate) throws StorageException {
        helper.update(
                DataQuery.STORE_OBJECT_MODERATE,
                messageId,
                userId,
                forumId,
                createDate
        );
    }

    public boolean removeModerateInfosByMessageId(int messageId) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECTS_MODERATE, messageId) > 0;
    }

    public IResult<Moderate> getModeratesByMessageId(int messageId) throws StorageException {
        return helper.execute(Converters.TO_MODERATE, DataQuery.GET_OBJECTS_MODERATE_BY_MESSAGE_ID, messageId);
    }

}
