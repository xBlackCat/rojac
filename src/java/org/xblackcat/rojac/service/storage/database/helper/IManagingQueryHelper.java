package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.IProgressTracker;
import org.xblackcat.rojac.service.storage.StorageException;

import java.util.Collection;

/**
 * @author ASUS
 */

public interface IManagingQueryHelper extends IQueryHelper {
    void shutdown();

    String getEngine();

    void updateBatch(String query, IProgressTracker tracker, Collection<Object[]> params) throws StorageException;

    IBatchedQueryHelper startBatch() throws StorageException;
}
