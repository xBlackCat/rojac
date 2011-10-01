package org.xblackcat.rojac.util;

import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;

import java.util.List;

/**
 * 15.09.11 16:43
 *
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.Background)
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
        // Replace storage engine before updating data in views.
        Storage.setStorage(settings);

        publish();

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void process(List<Void> chunks) {
        new ReloadDataPacket().dispatch();
    }
}
