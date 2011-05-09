package org.xblackcat.rojac.gui.dialog.shortcut;

import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.gui.dialog.LoginDialog;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.ShortCutUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author xBlackCat
 */
public class ShotCutManager extends JDialog {
    private ShortCutsTableModel shortCutsModel = new ShortCutsTableModel();

    public ShotCutManager(Window owner) {
        super(owner, ModalityType.MODELESS);
        setTitle("ShortCut manager");

        initializeLayout();

        setMinimumSize(new Dimension(300, 480));
        pack();
    }

    private void initializeLayout() {
        JPanel contentPane = new JPanel(new BorderLayout(5, 5));

        JTable table = new JTable(shortCutsModel);
        table.setTableHeader(null);
        contentPane.add(new JScrollPane(table));

        contentPane.add(WindowsUtils.createButtonsBar(
                        this,
                        Messages.Button_Ok,
                        new AnOkAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                applySettings();
                                setVisible(false);
                                dispose();
                            }
                        },
                        new AButtonAction(Messages.Button_Apply) {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                applySettings();
                            }
                        },
                        new ACancelAction() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                setVisible(false);
                                dispose();
                            }
                        }
                ), BorderLayout.SOUTH);


        setContentPane(contentPane);
    }

    private void applySettings() {
        shortCutsModel.commitChanges();

        ShortCutUtils.updateShortCuts(getOwner());
    }
}
