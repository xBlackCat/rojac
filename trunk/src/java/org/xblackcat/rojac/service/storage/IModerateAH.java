package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Moderate;
import ru.rsdn.janus.JanusModerateInfo;

/**
 * @author ASUS
 */

public interface IModerateAH extends AH {
    void storeModerateInfo(JanusModerateInfo mi) throws StorageException;

    boolean removeModerateInfosByMessageId(int id) throws StorageException;

    org.xblackcat.rojac.service.storage.IResult<Moderate> getModeratesByMessageId(int messageId) throws StorageException;

}
