package org.xblackcat.sunaj.service.synchronizer;

/**
 * Date: 12 трав 2007
 *
 * @author ASUS
 */

public interface ISynchronizerFactory {
    ISynchronizer getSynchronizer() throws SynchronizationException;
}
