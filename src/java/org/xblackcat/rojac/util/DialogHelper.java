package org.xblackcat.rojac.util;

import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.gui.dialog.AboutDialog;
import org.xblackcat.rojac.gui.dialog.db.DBSettingsDialog;
import org.xblackcat.rojac.gui.dialog.options.OptionsDialog;
import org.xblackcat.rojac.gui.dialog.subscribtion.SubscriptionDialog;
import org.xblackcat.sjpu.storage.connection.DBConfig;

import java.awt.*;

/**
 * @author xBlackCat
 */

public class DialogHelper {
    private DialogHelper() {
    }

    public static void showAboutDialog(Window owner) {
        AboutDialog ad = new AboutDialog(owner);
        WindowsUtils.center(ad);
        ad.setVisible(true);
    }

    public static void showOptionsDialog(Window parent) {
        try {
            OptionsDialog od = new OptionsDialog(parent);

            WindowsUtils.center(od);
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
        assert RojacUtils.checkThread(true);

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
     * Returns a main window of Rojac or {@code null} if the window is not found.
     *
     * @return a main window of Rojac or {@code null} if the window is not found.
     */
    public static Window getMainWindow() {
        for (Frame f : Frame.getFrames()) {
            if (f instanceof MainFrame) {
                return f;
            }
        }
        return null;
    }

    public static void openForumSubscriptionDialog(Window frame) {
        openForumSubscriptionDialog(frame, null);
    }

    public static void openForumSubscriptionDialog(Window frame, Runnable onClose) {
        SubscriptionDialog dlg = new SubscriptionDialog(frame, onClose);

        WindowsUtils.center(dlg);

        dlg.setVisible(true);
    }

    public static DBConfig showDBSettingsDialog(Window mainFrame) {
        DBSettingsDialog dlg = new DBSettingsDialog(mainFrame);

        WindowsUtils.center(dlg);
        dlg.setVisible(true);

        return dlg.getSettings();
    }
}
