package org.xblackcat.rojac.gui.dialog.db;

import org.xblackcat.rojac.gui.component.ACancelAction;
import org.xblackcat.rojac.gui.component.AnOkAction;
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
    private JTextArea logArea = new JTextArea();
    private JProgressBar logProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);
    private final JButton cancelButton;
    private final JButton okButton;

    public ImportProcessDialog(Window owner, final Runnable cancelAction) {
        super(owner, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        logArea.setEditable(false);
        logProgress.setStringPainted(true);

        cancelButton = WindowsUtils.setupButton(new ACancelAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelAction.run();
            }
        });
        okButton = WindowsUtils.setupButton(new AnOkAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        okButton.setVisible(false);

        setContentPane(setupContentPane());
        pack();
        setSize(400, 240);
        setMinimumSize(getSize());
    }

    private JComponent setupContentPane() {
        JPanel cp = new JPanel(new BorderLayout(5, 5));

        cp.add(new JLabel("Developing in progress..."), BorderLayout.NORTH);

        JPanel trackPane = new JPanel(new BorderLayout(5, 5));
        trackPane.add(logProgress, BorderLayout.NORTH);
        trackPane.add(new JScrollPane(logArea), BorderLayout.CENTER);
        cp.add(trackPane, BorderLayout.CENTER);

        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        cp.add(buttonPane, BorderLayout.SOUTH);

        buttonPane.add(cancelButton);
        buttonPane.add(okButton);

        return cp;
    }

    public void logText(String str) {
        logArea.append(str + "\n");
    }

    public void done() {
        cancelButton.setVisible(false);
        okButton.setVisible(true);
    }

    public void setProgress(int value, int bound) {
        logProgress.getModel().setMaximum(bound);
        logProgress.getModel().setValue(value);
        logProgress.setString(value + " / " + bound);
    }

    public void setStringPainted(boolean b) {
        logProgress.setStringPainted(b);
    }
}
