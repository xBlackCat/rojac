package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.service.storage.IUserAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.Converters;

/**
 * @author ASUS
 */

final class DBUserAH implements IUserAH {
    private final IQueryExecutor helper;

    DBUserAH(IQueryExecutor helper) {
        this.helper = helper;
    }

    @Override
    public void storeUserInfo(int userId, String userName) throws StorageException {
        helper.update(DataQuery.STORE_OBJECT_USER,
                userId,
                userName,
                userName,
                null,
                null,
                null,
                null,
                null,
                null);
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

    @Override
    public void updateUser(User u) throws StorageException {
        helper.update(DataQuery.UPDATE_OBJECT_USER,
                u.getUserName(),
                u.getUserNick(),
                u.getRealName(),
                u.getPublicEmail(),
                u.getHomePage(),
                u.getSpecialization(),
                u.getWhereFrom(),
                u.getOrigin(),
                u.getId());
    }

    public boolean removeUser(int id) throws StorageException {
        return helper.update(DataQuery.REMOVE_OBJECT_USER, id) > 0;
    }

    public User getUserById(int id) throws StorageException {
        return helper.executeSingle(Converters.TO_USER, DataQuery.GET_OBJECT_USER, id);
    }

    public int[] getAllUserIds() throws StorageException {
        return helper.getIds(DataQuery.GET_IDS_USER);
    }
}
