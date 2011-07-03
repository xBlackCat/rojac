package org.xblackcat.rojac.gui.dialog;

import org.xblackcat.rojac.gui.component.AnOkAction;
import org.xblackcat.rojac.gui.component.Fill;
import org.xblackcat.rojac.gui.component.JBackgroundPanel;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.util.RojacUtils;
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

        JBackgroundPanel panel = new JBackgroundPanel(ResourceUtils.loadImage("/images/rojac-logo.png"), Fill.RightBottom, new BorderLayout());
        cp.add(panel, BorderLayout.CENTER);
        JLabel infoArea = new JLabel();
        panel.add(infoArea);

        infoArea.setHorizontalAlignment(JLabel.CENTER);
        infoArea.setVerticalAlignment(JLabel.TOP);
        infoArea.setText(RojacUtils.VERSION_STRING);

        setContentPane(cp);

        setSize(350, 350);
        setResizable(false);
    }
}
