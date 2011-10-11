package org.xblackcat.rojac.service.storage.importing;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.dialog.db.ImportProcessDialog;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.IRowHandler;
import org.xblackcat.rojac.service.storage.database.IStructureChecker;
import org.xblackcat.rojac.service.storage.database.StructureChecker;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.service.storage.database.connection.SimpleConnectionFactory;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 11.10.11 10:37
 *
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.Background)
public class ImportProcessor extends RojacWorker<Void, ProgressChangeEvent> {
    private static final Log log = LogFactory.getLog(ImportProcessor.class);

    private final DatabaseSettings source;
    private final DatabaseSettings destination;

    private boolean stopped = false;

    private final ImportProcessDialog dlg;

    public ImportProcessor(DatabaseSettings source, DatabaseSettings destination, Window owner) {
        this.source = source;
        this.destination = destination;
        dlg = new ImportProcessDialog(owner, new Runnable() {
            @Override
            public void run() {
                setStopped(true);
            }
        });
        WindowsUtils.center(dlg);
    }

    private synchronized boolean isStopped() {
        return stopped;
    }

    private synchronized void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    @Override
    protected Void perform() throws Exception {
        IProgressListener progressListener = new IProgressListener() {
            @Override
            public void progressChanged(ProgressChangeEvent e) {
                publish(e);
            }
        };

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dlg.setVisible(true);
            }
        });

        try {
            IStructureChecker sourceChecker = new StructureChecker(source);
            IStructureChecker destinationChecker = new StructureChecker(destination);

            dlg.setStringPainted(false);
            publish(new ProgressChangeEvent(this, ProgressState.Work, Message.Log_ImportProgress_CheckSource.get(source.getEngine(), source.getUrl())));
            sourceChecker.check(progressListener);

            publish(new ProgressChangeEvent(this, ProgressState.Work, Message.Log_ImportProgress_CheckDestination.get(destination.getEngine(), destination.getUrl())));
            destinationChecker.check(progressListener);

            dlg.setStringPainted(true);
            // Copy data
            IImportHelper sourceStorage = new DBImportHelper(new SimpleConnectionFactory(source));
            final IImportHelper destinationStorage = new DBImportHelper(new SimpleConnectionFactory(destination));

            for (final String item : sourceStorage.getItemsList()) {
                final int sourceRows = sourceStorage.getRows(item);

                if (isStopped()) {
                    publish(new ProgressChangeEvent(this, ProgressState.Stop));
                    return null;
                }

                publish(new ProgressChangeEvent(this, ProgressState.Work, Message.Log_ImportProgress_CopyItem.get(item, sourceRows)));

                IRowWriter rowWriter = destinationStorage.getRowWriter(item);
                sourceStorage.getData(new RowHandler(rowWriter, sourceRows), item);
                rowWriter.stop();

                int destinationRows = destinationStorage.getRows(item);
                publish(new ProgressChangeEvent(this, ProgressState.Work, Message.Log_ImportProgress_CopyDone.get(item, destinationRows)));
            }

        } catch (Exception e) {
            log.error("Exception", e);
            publish(new ProgressChangeEvent(this, ProgressState.Exception, ExceptionUtils.getStackTrace(e)));
        }

        dlg.setStringPainted(false);
        publish(new ProgressChangeEvent(this, ProgressState.Stop, 1, 1, Message.Log_ImportProgress_Done.get()));

        return null;
    }

    @Override
    protected void process(List<ProgressChangeEvent> chunks) {
        for (ProgressChangeEvent event : chunks) {
            if (event.getText() != null) {
                dlg.logText(event.getText());
            }
            if (event.getValue() != null) {
                dlg.setProgress(event.getValue(), event.getBound());
            }
            if (event.getState() == ProgressState.Stop || event.getState() == ProgressState.Exception) {
                dlg.done();
            }
        }
    }

    @Override
    protected void done() {
        super.done();

        dlg.done();

        new ReloadDataPacket().dispatch();
    }

    private class RowHandler implements IRowHandler {
        private final IRowWriter rowWriter;
        private final int sourceRows;
        
        private int idx;

        public RowHandler(IRowWriter rowWriter, int sourceRows) {
            this.rowWriter = rowWriter;
            this.sourceRows = sourceRows;
        }

        @Override
        public boolean handleRow(Cell[] row) {
            try {
                rowWriter.storeRow(row);
                idx ++;
                publish(new ProgressChangeEvent(this, ProgressState.Work, idx, sourceRows));

                return !isStopped();
            } catch (StorageException e) {
                publish(new ProgressChangeEvent(this, ProgressState.Exception, ExceptionUtils.getStackTrace(e)));
                log.error("Can not store a row", e);
            }

            return false;
        }
    }
}
