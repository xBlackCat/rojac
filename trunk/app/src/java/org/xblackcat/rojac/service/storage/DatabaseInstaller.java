package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
import org.xblackcat.rojac.service.storage.database.DBStorage;
import org.xblackcat.rojac.service.storage.database.IStructureChecker;
import org.xblackcat.rojac.service.storage.database.StructureChecker;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.util.RojacWorker;

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
        this(null, settings);
    }

    public DatabaseInstaller(Runnable postProcessor, DatabaseSettings settings) {
        super(postProcessor);
        if (settings == null) {
            throw new NullPointerException("Invalid settings.");
        }

        this.settings = settings;
    }

    @Override
    protected Void perform() throws Exception {
        // Replace storage engine before updating data in views.
        IStructureChecker structureChecker = new StructureChecker(settings);

        structureChecker.check();

        Storage.setStorage(new DBStorage(settings));

        publish();

        return null;
    }

    @Override
    protected void process(List<Void> chunks) {
        new ReloadDataPacket().dispatch();
    }
}
