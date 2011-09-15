package org.xblackcat.rojac.util;

import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.database.DBStorage;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.connection.SimplePooledConnectionFactory;

import java.util.List;

/**
 * 15.09.11 16:43
 *
 * @author xBlackCat
 */
public class DatabaseInstaller extends RojacWorker<Void, Void> {
    private final DatabaseSettings settings;

    public DatabaseInstaller(DatabaseSettings settings) {
        this.settings = settings;
    }

    public DatabaseInstaller(Runnable postProcessor, DatabaseSettings settings) {
        super(postProcessor);
        this.settings = settings;
    }

    @Override
    protected Void perform() throws Exception {
        IConnectionFactory connectionFactory = new SimplePooledConnectionFactory(settings);

        DBStorage storage = new DBStorage(settings.getEngine(), connectionFactory);

        // Replace storage engine before updating data in views.
        Storage.setStorage(storage);

        publish();

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void process(List<Void> chunks) {
        new ReloadDataPacket().dispatch();
    }
}
