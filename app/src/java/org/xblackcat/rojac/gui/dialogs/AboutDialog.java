package org.xblackcat.rojac.gui.dialogs;

import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author xBlackCat
 */

class AboutDialog extends JDialog {
    public AboutDialog(Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);

        setTitle(Messages.DIALOG_ABOUT_TITLE.get());

        initializeLayout();
    }

    private void initializeLayout() {
        Component buttonsBar = WindowsUtils.createButtonsBar(this, Messages.BUTTON_OK, new AButtonAction(Messages.BUTTON_OK) {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel cp = new JPanel(new BorderLayout());

        cp.add(buttonsBar, BorderLayout.SOUTH);

        setContentPane(cp);
    }
}