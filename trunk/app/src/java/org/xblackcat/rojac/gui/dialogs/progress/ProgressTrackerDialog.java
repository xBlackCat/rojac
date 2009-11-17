package org.xblackcat.rojac.gui.dialogs.progress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */

public class ProgressTrackerDialog extends JDialog {
    private static final Log log = LogFactory.getLog(ProgressTrackerDialog.class);

    private JLabel statusMessage = new JLabel();
    private JTextArea logArea = new JTextArea();
    private JProgressBar logProgress = new JProgressBar();

    public ProgressTrackerDialog(Window mainFrame) {
        super(mainFrame, ModalityType.MODELESS);
        JPanel cp = new JPanel(new BorderLayout());

        cp.add(logArea, BorderLayout.CENTER);
        cp.add(logProgress, BorderLayout.SOUTH);

        setContentPane(cp);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        setSize(200, 100);
    }
}
