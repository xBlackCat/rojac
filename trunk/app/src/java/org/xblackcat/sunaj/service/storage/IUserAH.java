package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.data.User;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IUserAH extends AH {
    void storeUser(User ui) throws StorageException;

    boolean removeUser(int id) throws StorageException;

    User getUserById(int id) throws StorageException;

    int[] getAllUserIds() throws StorageException;
}
