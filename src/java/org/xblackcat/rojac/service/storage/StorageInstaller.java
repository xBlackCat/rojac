package org.xblackcat.rojac.service.storage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.dialog.db.CheckProcessDialog;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;
import org.xblackcat.rojac.service.storage.database.DBConfig;
import org.xblackcat.rojac.util.DialogHelper;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.sjpu.storage.StorageException;

import javax.swing.*;
import java.awt.*;

/**
 * 15.09.11 16:43
 *
 * @author xBlackCat
 */
public class StorageInstaller extends DatabaseWorker {
    private final StorageSettings settings;
    private final Runnable shutDownAction;

    public StorageInstaller(DBConfig settings, Window window) {
        this(null, null, settings, window);
    }

    public StorageInstaller(Runnable postProcessor, Runnable shutDownAction, DBConfig settings, Window owner) {
        super(postProcessor, owner, new CheckProcessDialog(owner, Message.Dialog_CheckProgress_Structure_Title, Message.Dialog_CheckProgress_Structure_Label));
        this.shutDownAction = shutDownAction;

        assert RojacUtils.checkThread(true) : "Installer should be started in EventDispatcher thread";

        this.settings = settings;
    }

    @Override
    protected boolean doWork() throws StorageException {
        // Replace storage engine before updating data in views.

        try {
            if (settings == null) {
                throw new StorageInitializationException("No settings");
            }

            IStructureChecker structureChecker = Storage.getChecker(settings);
            structureChecker.check(new IProgressListener() {
                @Override
                public void progressChanged(ProgressChangeEvent e) {
                    publish(e);
                }
            });
        } catch (final StorageException e) {
            final Log log = LogFactory.getLog(StorageInstaller.class);
            log.error("Can't check database structure.", e);

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    processDialog.dispose();

                    if (shutDownAction == null) {
                        RojacUtils.showExceptionDialog(e);
                        return;
                    }

                    DBConfig settings = DialogHelper.showDBSettingsDialog(owner);

                    if (settings == null) {
                        JLOptionPane.showMessageDialog(
                                owner,
                                Message.ErrorDialog_StorageNotDefined_Message.get(),
                                Message.ErrorDialog_StorageNotDefined_Title.get(),
                                JOptionPane.OK_OPTION
                        );
                        shutDownAction.run();
                    }

                    Property.ROJAC_DATABASE_CONNECTION_SETTINGS.set(settings);

                    // Restart database installer
                    new StorageInstaller(postProcessor, shutDownAction, settings, owner).execute();
                }
            });
            return false;
        }
        publish(new ProgressChangeEvent(this, ProgressState.Work, 1, 1));

        Storage.setStorage(settings);
        return true;
    }

    @Override
    protected void onSuccess() {
        new ReloadDataPacket().dispatch();
    }
}
