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
    private JTextArea logArea = new JTextArea();
    private JProgressBar logProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);

    public ImportProcessDialog(Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);

        logArea.setEditable(false);
        logProgress.setStringPainted(true);

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

        cp.add(WindowsUtils.createButtonsBar(FlowLayout.CENTER, new ACancelAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }), BorderLayout.SOUTH);

        return cp;
    }

    public void setProgressText(String str) {
        logArea.append(str + "\n");
    }
}
