package org.xblackcat.rojac.gui.dialog.db;

import org.xblackcat.rojac.gui.dialog.AProcessDialog;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 06.10.11 10:40
 *
 * @author xBlackCat
 */
public class CheckProcessDialog extends AProcessDialog {

    public CheckProcessDialog(Window owner, Message title, Message progressInfoLabel) {
        super(owner, title);

        logProgress.setStringPainted(false);

        JPanel cp = new JPanel(new BorderLayout(5, 5));

        cp.setBorder(new EmptyBorder(5, 10, 5, 10));

        JLabel infoLabel = new JLabel(progressInfoLabel.get(), SwingConstants.CENTER);
        cp.add(infoLabel, BorderLayout.CENTER);

        cp.add(logProgress, BorderLayout.SOUTH);

        setContentPane(cp);
        setSize(400, 120);
        setMinimumSize(getSize());
    }

}
