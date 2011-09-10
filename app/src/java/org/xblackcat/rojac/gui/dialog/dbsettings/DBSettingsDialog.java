package org.xblackcat.rojac.gui.dialog.dbsettings;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.service.options.MultiUserOptionsService;
import org.xblackcat.rojac.service.options.OptionsServiceException;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

import static org.xblackcat.rojac.i18n.Message.Button_Cancel;
import static org.xblackcat.rojac.i18n.Message.Button_Save;

/**
 * @author xBlackCat
 */
public class DBSettingsDialog extends JDialog {
    private static final Log log = LogFactory.getLog(DBSettingsDialog.class);
    private DBSettingsPane settingsPane;

    public DBSettingsDialog(Window owner) {
        super(owner);

        try {
            initialize();
        } catch (Exception e) {
            throw new RojacDebugException("Can not load engine list", e);
        }
    }

    private void initialize() throws IOException {
        JPanel cp = new JPanel(new BorderLayout(5, 5));

        cp.add(WindowsUtils.createButtonsBar(
                this,
                Button_Save,
                new AButtonAction(Button_Save) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        settingsPane.getCurrentSettings();
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

        cp.add(new JLabel("Select database engine"), BorderLayout.NORTH);

        cp.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(cp);
    }

    public static void main(String[] args) throws OptionsServiceException {
        Property.setOptionsService(new MultiUserOptionsService());

        DBSettingsDialog dialog = new DBSettingsDialog(null);
        dialog.setVisible(true);
        dialog.pack();
        WindowsUtils.centerOnScreen(dialog);

    }
}
