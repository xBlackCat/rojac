package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.User;
import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.Sql;

/**
 * @author ASUS
 */

public interface IUserAH extends IAH {
    @Sql("INSERT INTO user(id, name, nick, real_name, email, home_page, specialization, where_from, origin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")
    void storeUser(
            int id,
            String userName,
            String userNick,
            String realName,
            String publicEmail,
            String homePage,
            String specialization,
            String whereFrom,
            String origin
    ) throws StorageException;

    @Sql("SELECT id, name, nick, real_name, email, home_page, specialization, where_from, origin FROM user WHERE id=?")
    User getUserById(int id) throws StorageException;

    @Sql("UPDATE user SET name=?, nick=?, real_name=?, email=?, home_page=?, specialization=?, where_from=?, origin=? WHERE id=?")
    void updateUser(
            String userName,
            String userNick,
            String realName,
            String publicEmail,
            String homePage,
            String specialization,
            String whereFrom,
            String origin,
            int id
    ) throws StorageException;

    @Sql("SELECT COUNT(id)>0 FROM user WHERE id=?")
    boolean isUserExists(int id) throws StorageException;
}
