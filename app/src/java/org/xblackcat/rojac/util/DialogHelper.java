package org.xblackcat.rojac.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.gui.dialogs.AboutDialog;
import org.xblackcat.rojac.gui.dialogs.OptionsDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author xBlackCat
 */

public class DialogHelper {
    private DialogHelper() {
    }

    public static void showAboutDialog(Window owner) {
        AboutDialog ad = new AboutDialog(owner);
        ad.pack();
        WindowsUtils.centerOnScreen(ad);
        ad.setVisible(true);
    }

    public static void showOptionsDialog(Window parent) {
        try {
            OptionsDialog od = new OptionsDialog(parent);

            WindowsUtils.center(od, parent);
            od.setVisible(true);
        } catch (RojacException ex) {
            showExceptionDialog(ex);
        }
    }

    public static void showExceptionDialog(Throwable t) {
        showExceptionDialog(t, true);
    }

    public static void showExceptionDialog(Throwable t, boolean canBeIgnored) {
        showExceptionDialog(Thread.currentThread(), t, canBeIgnored);
    }

    public static void showExceptionDialog(Thread thread, Throwable t) {
        showExceptionDialog(thread, t, true);
    }

    public static void showExceptionDialog(Thread thread, Throwable t, boolean canBeIgnored) {
        RojacUtils.checkThread(true, null);

        Window w = getMainWindow();
        ExceptionDialog dialog = new ExceptionDialog(w);
        dialog.initialize(thread, t, canBeIgnored);

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        size.height >>= 2;
        size.width >>= 2;

        dialog.setSize(size);
        WindowsUtils.centerOnScreen(dialog);
        dialog.setVisible(true);
    }

    /**
     * Returns a main window of Rojac or <code>null</code> if the window is not found.
     *
     * @return a main window of Rojac or <code>null</code> if the window is not found.
     */
    public static Window getMainWindow() {
        for (Frame f : Frame.getFrames()) {
            if (f instanceof MainFrame) {
                return f;
            }
        }
        return null;
    }

    /**
     * @author xBlackCat
     */

    private static class ExceptionDialog extends JDialog {
        public ExceptionDialog(Window owner) {
            super(owner, ModalityType.APPLICATION_MODAL);
        }

        private void initialize(Thread thread, Throwable t, boolean canBeIgnored) {
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
            str.append(thread);
            str.append("\n\n");
            str.append(ExceptionUtils.getFullStackTrace(t));
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
}
