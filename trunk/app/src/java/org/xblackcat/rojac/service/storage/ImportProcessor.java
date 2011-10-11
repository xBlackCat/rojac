package org.xblackcat.rojac.service.storage;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.Cell;
import org.xblackcat.rojac.gui.dialog.db.ImportProcessDialog;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
import org.xblackcat.rojac.service.storage.database.DBImportHelper;
import org.xblackcat.rojac.service.storage.database.IRowHandler;
import org.xblackcat.rojac.service.storage.database.IStructureChecker;
import org.xblackcat.rojac.service.storage.database.StructureChecker;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.service.storage.database.connection.SimpleConnectionFactory;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
 * 11.10.11 10:37
 *
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.Background)
public class ImportProcessor extends RojacWorker<Void, String> {
    private static final Log log = LogFactory.getLog(ImportProcessor.class);

    private final DatabaseSettings source;
    private final DatabaseSettings destination;

    private final ImportProcessDialog dlg;

    public ImportProcessor(DatabaseSettings source, DatabaseSettings destination, ImportProcessDialog dlg) {
        this.source = source;
        this.destination = destination;
        this.dlg = dlg;
    }

    @Override
    protected Void perform() throws Exception {
        try {
            IStructureChecker sourceChecker = new StructureChecker(source);
            IStructureChecker destinationChecker = new StructureChecker(destination);

            publish("Check source...");
            sourceChecker.check();

            publish("Check destination...");
            destinationChecker.check();

            // Copy data
            IImportHelper sourceStorage = new DBImportHelper(new SimpleConnectionFactory(source));
            final IImportHelper destinationStorage = new DBImportHelper(new SimpleConnectionFactory(destination));

            for (final String item : sourceStorage.getItemsList()) {
                int sourceRows = sourceStorage.getRows(item);

                publish("Copy " + item + " (" + sourceRows + ")");

                sourceStorage.getData(new IRowHandler() {
                    @Override
                    public boolean handleRow(Cell[] row) {
                        try {
                            destinationStorage.storeItem(item, row);
                            return true;
                        } catch (StorageException e) {
                            publish(ExceptionUtils.getStackTrace(e));
                            log.error("Can not store a row", e);
                        }

                        return false;
                    }
                }, item);

                int destinationRows = destinationStorage.getRows(item);
                publish("Copied. Destination size: " + destinationRows);
            }

        } catch (Exception e) {
            log.error("Exception", e);
            publish(ExceptionUtils.getStackTrace(e));
        }

        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        for (String str : chunks) {
            dlg.setProgressText(str);
        }
    }
}
