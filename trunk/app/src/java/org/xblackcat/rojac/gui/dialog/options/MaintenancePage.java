package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.dialog.db.ImportDialog;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.storage.DatabaseCacheUpdater;
import org.xblackcat.rojac.service.storage.DatabaseCleaner;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 06.04.12 18:23
 *
 * @author xBlackCat
 */
class MaintenancePage extends APage {
    MaintenancePage(final Window owner) {
        JPanel importData = buildActionPane(
                Message.Dialog_Options_Action_Import_Title,
                Message.Dialog_Options_Action_Import_Info,
                new AButtonAction(Message.Dialog_Options_Action_Import_Button) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ImportDialog importDialog = new ImportDialog(owner);

                        WindowsUtils.center(importDialog, owner);
                        importDialog.setVisible(true);
                    }
                }
        );
        JPanel rebuildCache = buildActionPane(
                Message.Dialog_Options_Action_RebuildCache_Title,
                Message.Dialog_Options_Action_RebuildCache_Info,
                new AButtonAction(Message.Dialog_Options_Action_RebuildCache_Button) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new DatabaseCacheUpdater(owner).execute();
                    }
                }
        );
        JPanel cleanUpDatabase = buildActionPane(
                Message.Dialog_Options_Action_Clean_Title,
                Message.Dialog_Options_Action_Clean_Info,
                new AButtonAction(Message.Dialog_Options_Action_Clean_Button) {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        CleanSettingsDialog dialog = new CleanSettingsDialog(owner);
                        Long period = dialog.getPeriod();
                        dialog.dispose();
                        if (period != null) {
                            new DatabaseCleaner(owner, period).execute();
                        }
                    }
                }
        );

        JPanel panes = WindowsUtils.createColumn(
                5,
                5,
                importData,
                rebuildCache,
                cleanUpDatabase
        );

        add(panes, BorderLayout.NORTH);
    }

    private static JPanel buildActionPane(Message title, Message description, AButtonAction action) {
        JPanel maintenancePane = new JPanel(new BorderLayout(5, 5));
        maintenancePane.setBorder(new TitledBorder(null, title.get(), TitledBorder.LEFT, TitledBorder.TOP));

        JLabel hint = new JLabel(description.get());
        hint.setBorder(new EmptyBorder(5, 10, 5, 10));
        maintenancePane.add(hint, BorderLayout.NORTH);

        maintenancePane.add(WindowsUtils.setupButton(action), BorderLayout.EAST);
        return maintenancePane;
    }

    @Override
    protected void applySettings(Window mainFrame) {
    }

    @Override
    public Message getTitle() {
        return Message.Dialog_Options_Title_Tools;
    }

    @Override
    public void placeFocus() {
    }

}
