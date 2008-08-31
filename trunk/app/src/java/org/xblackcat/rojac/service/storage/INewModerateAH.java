package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.NewModerate;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

public interface INewModerateAH extends AH {
    void storeNewModerate(NewModerate nm) throws StorageException;

    boolean removeNewModerate(int id) throws StorageException;

    NewModerate getNewModerateById(int id) throws StorageException;

    NewModerate[] getAllNewModerates() throws StorageException;
}