package org.xblackcat.rojac.gui.dialogs;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */

public class ExceptionDialog extends JDialog {
    public ExceptionDialog(Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);
    }

    public static void showExceptionDialog(Throwable t) {
        showExceptionDialog(Thread.currentThread(), t);
    }

    public static void showExceptionDialog(Thread thread, Throwable t) {
        RojacUtils.checkThread(true, null);

        Window w = getMainWindow();
        ExceptionDialog dialog = new ExceptionDialog(w);
        dialog.initialize(thread, t);

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
    private static Window getMainWindow() {
        for (Frame f : Frame.getFrames()) {
            if (f instanceof MainFrame) {
                return f;
            }
        }
        return null;
    }

    private void initialize(Thread thread, Throwable t) {
        JPanel cp = new JPanel(new BorderLayout());

        StringBuilder str = new StringBuilder();
        str.append(thread);
        str.append("\n\n");
        str.append(ExceptionUtils.getFullStackTrace(t));

        // Setup exception text.
        JTextArea exceptionText = new JTextArea();
        exceptionText.setEditable(false);
        exceptionText.setText(str.toString());
        cp.add(new JScrollPane(exceptionText), BorderLayout.CENTER);

        cp.add(new JLabel("Unhandled exception is raised."), BorderLayout.NORTH);

        setContentPane(cp);

        pack();
    }
}
