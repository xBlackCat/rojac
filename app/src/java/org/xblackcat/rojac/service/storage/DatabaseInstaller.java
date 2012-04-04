package org.xblackcat.rojac.service.storage;

import org.xblackcat.rojac.gui.dialog.db.CheckProcessDialog;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.progress.IProgressListener;
import org.xblackcat.rojac.service.progress.ProgressChangeEvent;
import org.xblackcat.rojac.service.progress.ProgressState;
import org.xblackcat.rojac.service.storage.database.DBStorage;
import org.xblackcat.rojac.service.storage.database.IStructureChecker;
import org.xblackcat.rojac.service.storage.database.StructureChecker;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.util.DialogHelper;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;
import java.awt.*;

/**
 * 15.09.11 16:43
 *
 * @author xBlackCat
 */
public class DatabaseInstaller extends DatabaseWorker {
    private final DatabaseSettings settings;
    private final Runnable shutDownAction;

    public DatabaseInstaller(DatabaseSettings settings, Window window) {
        this(null, null, settings, window);
    }

    public DatabaseInstaller(Runnable postProcessor, Runnable shutDownAction, DatabaseSettings settings, Window owner) {
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

            IStructureChecker structureChecker = new StructureChecker(settings);
            structureChecker.check(new IProgressListener() {
                @Override
                public void progressChanged(ProgressChangeEvent e) {
                    publish(e);
                }
            });
        } catch (final StorageException e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    processDialog.dispose();

                    if (shutDownAction == null) {
                        RojacUtils.showExceptionDialog(e);
                        return;
                    }

                    DatabaseSettings settings = DialogHelper.showDBSettingsDialog(owner);

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
                    new DatabaseInstaller(postProcessor, shutDownAction, settings, owner).execute();
                }
            });
            return false;
        }
        publish(new ProgressChangeEvent(this, ProgressState.Work, 1, 1));

        Storage.setStorage(new DBStorage(settings));
        return true;
    }

    @Override
    protected void onSuccess() {
        new ReloadDataPacket().dispatch();
    }
}
