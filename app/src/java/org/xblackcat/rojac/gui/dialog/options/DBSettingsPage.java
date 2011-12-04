package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.dialog.db.DBSettingsPane;
import org.xblackcat.rojac.gui.dialog.db.ImportDialog;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.DatabaseInstaller;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 15.09.11 16:33
 *
 * @author xBlackCat
 */
public class DBSettingsPage extends APage {
    private final DBSettingsPane settingsPane;

    public DBSettingsPage(Window owner) {
        JButton migrateButton = WindowsUtils.setupButton(new ImportAction(owner));
        settingsPane = new DBSettingsPane(migrateButton);
        add(settingsPane, BorderLayout.NORTH);
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
