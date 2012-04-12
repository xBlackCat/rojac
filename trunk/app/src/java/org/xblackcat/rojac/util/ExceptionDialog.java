package org.xblackcat.rojac.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author xBlackCat
 */
class ExceptionDialog extends JDialog {
    public ExceptionDialog(Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);
    }

    void initialize(Thread thread, Throwable t, boolean canBeIgnored) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (!canBeIgnored) {
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    System.exit(-1);
                }
            });
        }

        JPanel cp = new JPanel(new BorderLayout());

        StringBuilder str = new StringBuilder();
        // Add system info.
        str.append(RojacUtils.VERSION_STRING);
        str.append("\nSystem: ");
        str.append(SystemUtils.OS_NAME);
        str.append(" ");
        str.append(SystemUtils.OS_ARCH);
        str.append(" ");
        str.append(SystemUtils.OS_VERSION);
        try {
            String osPatchLevel = System.getProperty("sun.os.patch.level");
            if (StringUtils.isNotBlank(osPatchLevel)) {
                str.append(" (");
                str.append(osPatchLevel);
                str.append(")");
            }
        } catch (SecurityException e) {
            // Ignore patch level
        }
        str.append("\n");
        str.append(SystemUtils.JAVA_RUNTIME_NAME);
        str.append(" ");
        str.append(SystemUtils.JAVA_RUNTIME_VERSION);
        str.append(" (");
        str.append(SystemUtils.JAVA_VENDOR);
        str.append(")\n\n");

        str.append(thread);
        str.append("\n\n");
        str.append(ExceptionUtils.getStackTrace(t));
        final String stackTrace = str.toString();

        // Setup exception text.
        JTextArea exceptionText = new JTextArea();
        exceptionText.setEditable(false);
        exceptionText.setText(stackTrace);
        cp.add(new JScrollPane(exceptionText), BorderLayout.CENTER);

        cp.add(new JLabel("Unhandled exception is raised."), BorderLayout.NORTH);

        JPanel buttonsPane = new JPanel(new GridLayout(1, 0, 5, 0));

        buttonsPane.add(new JButton(new AbstractAction("To clipboard") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClipboardUtils.copyToClipboard(stackTrace);
            }
        }));
        buttonsPane.add(new JButton(new AbstractAction("Shutdown") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(-1);
            }
        }));
        if (canBeIgnored) {
            buttonsPane.add(new JButton(new AbstractAction("Close") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            }));
        }

        cp.add(WindowsUtils.coverComponent(buttonsPane), BorderLayout.SOUTH);

        setContentPane(cp);

        pack();
    }
}
