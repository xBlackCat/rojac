package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.service.data.Moderate;
import org.xblackcat.sunaj.service.storage.IModerateDAO;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.ToModerateConvertor;

import java.util.Collection;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

final class DBModerateDAO implements IModerateDAO {
    private final IQueryExecutor helper;

    DBModerateDAO(IQueryExecutor helper) {
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
        Collection<Moderate> m = helper.execute(new ToModerateConvertor(), DataQuery.GET_OBJECTS_MODERATE_BY_MESSAGE_ID, messageId);
        return m.toArray(new Moderate[m.size()]);
    }

}
