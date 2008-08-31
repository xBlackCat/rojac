package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Moderate;

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
