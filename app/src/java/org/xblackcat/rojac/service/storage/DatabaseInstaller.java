package org.xblackcat.rojac.service.storage;

import org.bridj.cpp.com.shell.ITaskbarList3;
import org.xblackcat.rojac.gui.component.Windows7Bar;
import org.xblackcat.rojac.gui.dialog.db.CheckProcessDialog;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.ReloadDataPacket;
import org.xblackcat.rojac.service.executor.TaskType;
import org.xblackcat.rojac.service.executor.TaskTypeEnum;
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
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 15.09.11 16:43
 *
 * @author xBlackCat
 */
@TaskType(TaskTypeEnum.Background)
public class DatabaseInstaller extends RojacWorker<Void, ProgressChangeEvent> {
    private final DatabaseSettings settings;
    private final CheckProcessDialog dlg;
    private final Runnable shutDownAction;
    private final Window owner;

    private final Windows7Bar win7bar;

    public DatabaseInstaller(DatabaseSettings settings, Window window) {
        this(null, null, settings, window);
    }

    public DatabaseInstaller(Runnable postProcessor, Runnable shutDownAction, DatabaseSettings settings, Window owner) {
        this(postProcessor, shutDownAction, settings, owner, Windows7Bar.getWindowBar(owner));
    }

    public DatabaseInstaller(Runnable postProcessor, Runnable shutDownAction, DatabaseSettings settings, Window owner, Windows7Bar win7bar) {
        super(postProcessor);
        this.shutDownAction = shutDownAction;
        this.owner = owner;

        assert RojacUtils.checkThread(true) : "Installer should be started in EventDispatcher thread";

        this.settings = settings;

        dlg = new CheckProcessDialog(owner);
        this.win7bar = win7bar;
    }

    @Override
    protected Void perform() throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                WindowsUtils.center(dlg);
                dlg.setVisible(true);
            }
        });

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
                    dlg.dispose();

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
            return null;
        }
        publish(new ProgressChangeEvent(this, ProgressState.Work, 1, 1));

        Storage.setStorage(new DBStorage(settings));

        publish(new ProgressChangeEvent(this, ProgressState.Stop));

        return null;
    }

    @Override
    protected void process(List<ProgressChangeEvent> chunks) {
        for (ProgressChangeEvent e : chunks) {
            if (e.getState() == ProgressState.Stop) {
                dlg.dispose();
                if (win7bar != null) {
                    win7bar.setState(ITaskbarList3.TbpFlag.TBPF_NOPROGRESS);
                }

                new ReloadDataPacket().dispatch();
            } else if (e.getBound() != null && e.getValue() != null) {
                dlg.setProgress(e.getValue(), e.getBound());
                if (win7bar != null) {
                    win7bar.setState(ITaskbarList3.TbpFlag.TBPF_PAUSED);
                    win7bar.setProgress(e.getValue(), e.getBound());
                }
            }
        }
    }
}
