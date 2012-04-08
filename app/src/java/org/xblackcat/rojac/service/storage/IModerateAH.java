package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.data.Moderate;
import ru.rsdn.Janus.JanusModerateInfo;

/**
 * @author ASUS
 */

public interface IModerateAH extends AH {
    void storeModerateInfo(JanusModerateInfo mi) throws StorageException;

    boolean removeModerateInfosByMessageId(int id) throws StorageException;

    Iterable<Moderate> getModeratesByMessageId(int messageId) throws StorageException;

}
