package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Moderate;

/**
 * @author ASUS
 */

public interface IModerateAH extends AH {
    void storeModerateInfo(int messageId, int userId, int forumId, long createDate) throws StorageException;

    boolean removeModerateInfosByMessageId(int id) throws StorageException;

    org.xblackcat.rojac.service.storage.IResult<Moderate> getModeratesByMessageId(int messageId) throws StorageException;

}
