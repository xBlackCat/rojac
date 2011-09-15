package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.gui.dialog.dbsettings.DBSettingsPane;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.util.DatabaseInstaller;

import java.awt.*;

/**
 * 15.09.11 16:33
 *
 * @author xBlackCat
 */
public class DBSettingsPage extends APage {
    private DBSettingsPane settingsPane = new DBSettingsPane();

    public DBSettingsPage() {
        add(settingsPane, BorderLayout.CENTER);
    }

    @Override
    protected void applySettings(Window mainFrame) {
        final DatabaseSettings currentSettings = settingsPane.getCurrentSettings();
        if (currentSettings == null) {
            return;
        }

        Property.ROJAC_DATABASE_CONNECTION_SETTINGS.set(currentSettings);

        new DatabaseInstaller(currentSettings).execute();
    }

    @Override
    public Message getTitle() {
        return Message.Dialog_DbSettings_Title;
    }

    @Override
    public void placeFocus() {
        settingsPane.requestFocusInField();
    }
}
