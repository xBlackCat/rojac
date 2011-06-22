package org.xblackcat.rojac.gui.dialog;

import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.WindowsUtils;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author xBlackCat
 */

public class AboutDialog extends JDialog {
    public AboutDialog(Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);

        setTitle(Message.Dialog_About_Title.get());

        initializeLayout();
    }

    private void initializeLayout() {
        Component buttonsBar = WindowsUtils.createButtonsBar(this, Message.Button_Ok, new AnOkAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        JPanel cp = new JPanel(new BorderLayout());

        cp.add(buttonsBar, BorderLayout.SOUTH);

        cp.add(new JLabel(ResourceUtils.loadIcon("/images/rojac-logo.png")), BorderLayout.CENTER);

        setContentPane(cp);
    }
}
