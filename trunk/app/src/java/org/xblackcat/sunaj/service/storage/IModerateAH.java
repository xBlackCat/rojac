package org.xblackcat.sunaj.service.storage;

import org.xblackcat.sunaj.data.Moderate;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface IModerateAH extends AH {
    void storeModerateInfo(Moderate mi) throws StorageException;

    boolean removeModerateInfosByMessageId(int id) throws StorageException;

    Moderate[] getModeratesByMessageId(int messageId) throws StorageException;

}
