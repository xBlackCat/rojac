package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.service.progress.IProgressListener;

/**
 * 27.09.11 17:17
 *
 * @author xBlackCat
 */
public interface IStructureChecker {

    /**
     * Checks and initialize database structure.
     *
     * @param progressListener
     * @throws StorageCheckException
     */
    void check(IProgressListener progressListener) throws StorageCheckException;
}
