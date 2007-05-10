package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.service.data.Moderate;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IModerateDAO {
    void storeModerateInfo(Moderate mi) throws StorageException;

    boolean removeModerateInfosByMessageId(int id) throws StorageException;

    Moderate[] getModeratesByMessageId(int messageId) throws StorageException;

}
