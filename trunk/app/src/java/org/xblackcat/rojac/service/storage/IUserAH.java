package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.User;

/**
 * @author ASUS
 */

public interface IUserAH extends AH {
    void storeUser(User ui) throws StorageException;

    boolean removeUser(int id) throws StorageException;

    User getUserById(int id) throws StorageException;

    void storeUserInfo(int userId, String userName) throws StorageException;

    void updateUser(User u) throws StorageException;
}
