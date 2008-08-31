package org.xblackcat.rojac.service.synchronizer;

/**
 * Date: 12 трав 2007
 *
 * @author ASUS
 */

public interface ISynchronizer {
    void synchronize() throws SynchronizationException;

    void updateForumList() throws SynchronizationException;
}
