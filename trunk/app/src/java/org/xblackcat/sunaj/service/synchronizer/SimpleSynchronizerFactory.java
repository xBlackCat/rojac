package org.xblackcat.sunaj.service.synchronizer;

import org.xblackcat.sunaj.service.storage.IStorage;

/**
 * Date: 12 трав 2007
 *
 * @author ASUS
 */

public class SimpleSynchronizerFactory implements ISynchronizerFactory {
    private final String login;
    private final String password;
    private final IStorage storage;

    public SimpleSynchronizerFactory(String login, String password, IStorage storage) {
        this.storage = storage;
        this.login = login;
        this.password = password;
    }

    public ISynchronizer getSynchronizer() throws SynchronizationException {
        return new SimpleSynchronizer(login, password, storage);
    }
}
