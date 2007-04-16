package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.ModerateInfo;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IModerateDAO {
    void storeModerateInfo(ModerateInfo mi) throws StorageException;

    boolean removeModerateInfo(int id) throws StorageException;

    ModerateInfo getModerateInfoById(int id) throws StorageException;

    int[] getModerateIdsByMessageId(int messageId) throws StorageException;

    int[] getModerateIdsByUserIds(int userId) throws StorageException;

    int[] getModerateIdsByForumIds(int forumId) throws StorageException;

    int[] getAllModerateIds() throws StorageException;
}
