package org.xblackcat.rojac.gui.dialog.db;

import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * 06.10.11 10:40
 *
 * @author xBlackCat
 */
public class ImportProcessDialog extends JDialog {
    public ImportProcessDialog(Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);

        setContentPane(setupContentPane());
        pack();
    }

    private JComponent setupContentPane() {
        JPanel cp = new JPanel(new BorderLayout(5, 5));

        cp.add(new JLabel("Developing in progress..."), BorderLayout.CENTER);

        cp.add(WindowsUtils.createButtonsBar(FlowLayout.CENTER, new ACancelAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }), BorderLayout.SOUTH);

        return cp;
    }
}
