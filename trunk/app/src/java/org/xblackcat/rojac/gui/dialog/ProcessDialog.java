package org.xblackcat.rojac.gui.dialog;

import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.awt.*;

/**
 * 06.04.12 13:23
 *
 * @author xBlackCat
 */
public class ProcessDialog extends JDialog {
    protected final JProgressBar logProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);

    public ProcessDialog(Window owner, Message title) {
        super(owner, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setTitle(title.get());
    }

    public void setProgress(long value, long bound) {
        while (bound > Integer.MAX_VALUE) {
            bound >>= 1;
            value >>= 1;
        }

        setProgress((int) value, (int) bound);
    }

    public void setProgress(int value, int bound) {
        logProgress.setIndeterminate(false);
        logProgress.getModel().setMaximum(bound);
        logProgress.getModel().setValue(value);
        logProgress.setString(value + " / " + bound);
    }

    public void setIndeterminate() {
        logProgress.setIndeterminate(true);
    }
}
