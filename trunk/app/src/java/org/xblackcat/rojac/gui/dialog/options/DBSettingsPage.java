package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.dialog.db.DBSettingsPane;
import org.xblackcat.rojac.gui.dialog.db.ImportDialog;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.DatabaseCacheUpdater;
import org.xblackcat.rojac.service.storage.DatabaseInstaller;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 15.09.11 16:33
 *
 * @author xBlackCat
 */
public class DBSettingsPage extends APage {
    private final DBSettingsPane settingsPane;

    public DBSettingsPage(final Window owner) {
        JPanel internalPane = new JPanel(new BorderLayout());
        JButton migrateButton = WindowsUtils.setupButton(new ImportAction(owner));
        settingsPane = new DBSettingsPane(migrateButton);

        add(internalPane, BorderLayout.NORTH);

        internalPane.add(settingsPane, BorderLayout.NORTH);

        JPanel rebuildCache = buildActionPane(
                Message.Dialog_Options_Action_RebuildCache_Title,
                Message.Dialog_Options_Action_RebuildCache_Info,
                new AbstractAction(Message.Dialog_Options_Action_RebuildCache_Button.get()) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new DatabaseCacheUpdater(owner).execute();
                    }
                }
        );
        JPanel cleanUpDatabase = buildActionPane(
                Message.Dialog_Options_Action_Clean_Title,
                Message.Dialog_Options_Action_Clean_Info,
                new AbstractAction(Message.Dialog_Options_Action_Clean_Button.get()) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    }
                }
        );

        internalPane.add(WindowsUtils.createColumn(5, 5, rebuildCache, cleanUpDatabase), BorderLayout.CENTER);
    }

    private static JPanel buildActionPane(Message title, Message description, Action action) {
        JPanel maintenancePane = new JPanel(new BorderLayout(5, 5));
        maintenancePane.setBorder(new TitledBorder(null, title.get(), TitledBorder.LEFT, TitledBorder.TOP));

        JLabel hint = new JLabel(description.get());
        hint.setBorder(new EmptyBorder(5, 10, 5, 10));
        maintenancePane.add(hint, BorderLayout.NORTH);
        maintenancePane.add(new JButton(action), BorderLayout.EAST);
        return maintenancePane;
    }

    @Override
    protected void applySettings(Window mainFrame) {
        DatabaseSettings currentSettings = Property.ROJAC_DATABASE_CONNECTION_SETTINGS.get();

        final DatabaseSettings newSettings = settingsPane.getCurrentSettings();
        if (newSettings == null) {
            return;
        }

        if (!currentSettings.equals(newSettings)) {
            Property.ROJAC_DATABASE_CONNECTION_SETTINGS.set(newSettings);

            new DatabaseInstaller(newSettings, mainFrame).execute();
        }
    }

    @Override
    public Message getTitle() {
        return Message.Dialog_DbSettings_Title;
    }

    @Override
    public void placeFocus() {
        settingsPane.requestFocusInField();
    }

    private class ImportAction extends AButtonAction {
        private final Window owner;

        public ImportAction(Window owner) {
            super(Message.Button_Import);
            this.owner = owner;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ImportDialog importDialog = new ImportDialog(owner);

            WindowsUtils.center(importDialog, owner);
            importDialog.setVisible(true);
        }
    }
}
