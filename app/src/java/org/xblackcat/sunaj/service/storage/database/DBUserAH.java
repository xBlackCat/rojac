package org.xblackcat.sunaj.service.storage.database;

import org.xblackcat.sunaj.data.User;
import org.xblackcat.sunaj.service.storage.IUserAH;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.Converters;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

final class DBUserAH implements IUserAH {
    private final IQueryExecutor helper;

    DBUserAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    public void storeUser(User u) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_USER,
                u.getId(),
                u.getUserName(),
                u.getUserNick(),
                u.getRealName(),
                u.getPublicEmail(),
                u.getHomePage(),
                u.getSpecialization(),
                u.getWhereFrom(),
                u.getOrigin());
    }

    public boolean removeUser(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_USER, id) > 0;
    }

    public User getUserById(int id) throws StorageException {
        return helper.executeSingle(Converters.TO_USER_CONVERTER, DataQuery.GET_OBJECT_USER, id);
    }

    public int[] getAllUserIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_USER);
    }
}
