package org.xblackcat.sunaj.service.storage.database;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.sunaj.service.data.User;
import org.xblackcat.sunaj.service.storage.IUserDAO;
import org.xblackcat.sunaj.service.storage.StorageDataException;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.convert.ToScalarConvertor;
import org.xblackcat.sunaj.service.storage.database.convert.ToUserConvertor;

import java.util.Collection;

/**
 * Date: 10 трав 2007
 *
 * @author ASUS
 */

public class DBUserDAO implements IUserDAO {
    private final IQueryExecutor helper;

    public DBUserDAO(IQueryExecutor helper) {
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
        return helper.executeSingle(new ToUserConvertor(), DataQuery.GET_OBJECT_USER, id);
    }

    public int[] getAllUserIds() throws StorageException {
        Collection<Integer> objIds = helper.execute(new ToScalarConvertor<Integer>(), DataQuery.GET_IDS_USER);
        int[] ids;

        try {
            // Conver collection of Integer to array of int.
            ids = ArrayUtils.toPrimitive(objIds.toArray(new Integer[objIds.size()]));
        } catch (NullPointerException e) {
            throw new StorageDataException("Got null instead of real value.", e);
        }

        return ids;
    }
}
