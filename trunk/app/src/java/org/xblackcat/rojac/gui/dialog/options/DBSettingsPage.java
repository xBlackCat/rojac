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
        DatabaseSettings currentSettings = Property.ROJAC_DATABASE_CONNECTION_SETTINGS.get();

        final DatabaseSettings newSettings = settingsPane.getCurrentSettings();
        if (newSettings == null) {
            return;
        }

        if (!currentSettings.equals(newSettings)) {
            Property.ROJAC_DATABASE_CONNECTION_SETTINGS.set(newSettings);

            new DatabaseInstaller(newSettings).execute();
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
}
