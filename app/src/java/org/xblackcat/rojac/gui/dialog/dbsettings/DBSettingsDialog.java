package org.xblackcat.rojac.gui.dialog.dbsettings;

import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import static org.xblackcat.rojac.i18n.Message.*;

/**
 * @author xBlackCat
 */
public class DBSettingsDialog extends JDialog {
    private DBSettingsPane settingsPane;
    private DatabaseSettings settings;

    public DBSettingsDialog(Window owner) {
        super(owner, Dialog_DbSettings_Title.get(), ModalityType.APPLICATION_MODAL);

        initialize();

        pack();

        setSize(350, getHeight());
    }

    private void initialize() {
        JPanel cp = new JPanel(new BorderLayout(5, 5));

        cp.add(WindowsUtils.createButtonsBar(
                this,
                Button_Save,
                new AButtonAction(Button_Save) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        settings = settingsPane.getCurrentSettings();

                        dispose();
                    }
                },
                new AButtonAction(Button_Cancel) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                }
        ), BorderLayout.SOUTH);

        settingsPane = new DBSettingsPane();
        cp.add(settingsPane, BorderLayout.CENTER);

        cp.add(new JLabel(Dialog_DbSettings_Label.get(), SwingConstants.CENTER), BorderLayout.NORTH);

        cp.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(cp);
    }

    public DatabaseSettings getSettings() {
        return settings;
    }
}
