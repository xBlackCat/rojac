package org.xblackcat.rojac;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.gui.tray.RojacTray;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.LocaleControl;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.progress.LoggingProgressListener;
import org.xblackcat.rojac.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static org.xblackcat.rojac.service.options.Property.*;

/**
 * @author Alexey
 */

public final class RojacLauncher {
    private static final Log log = LogFactory.getLog(RojacLauncher.class);

    private RojacLauncher() {
    }

    public static void main(String[] args) {
        try {
            launch();
        } catch (Exception e) {
            log.fatal("Can not initialize Rojac", e);
        }
    }

    private static void launch() throws Exception {
        // Common tasks
        Thread.setDefaultUncaughtExceptionHandler(RojacUtils.GLOBAL_EXCEPTION_HANDLER);

        // Initialize core services
        ServiceFactory.initialize();

        // Set up debug mode if set
        if (ROJAC_DEBUG_MODE.get()) {
            ServiceFactory.getInstance()
                    .getProgressControl()
                    .addProgressListener(new LoggingProgressListener());
        }

        LookAndFeel laf = ROJAC_GUI_LOOK_AND_FEEL.get();
        try {
            if (log.isDebugEnabled()) {
                log.debug("Using LAF: " + laf.getName());
            }
            UIUtils.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RojacException("Can not initialize " + laf.getName() + " L&F.", e);
        }

        LocaleControl.getInstance().setLocale(ROJAC_GUI_LOCALE.get());

        ShortCutUtils.loadShortCuts();

        SwingUtilities.invokeLater(new SwingPartInitializer());
    }

    private static void storeSettings() {
        ShortCutUtils.storeShortCuts();

        if (!RSDN_USER_PASSWORD_SAVE.get()) {
            RSDN_USER_PASSWORD.clear();
        }

        ServiceFactory.getInstance().getOptionsService().storeSettings();
    }

    private static void setupUserSettings() {
        // TODO: Load user settings from options
    }

    private static class SwingPartInitializer implements Runnable {
        public void run() {
            try {
                perform();
            } catch (Exception e) {
                log.fatal("Can not initialize Swing part of Rojac", e);
                DialogHelper.showExceptionDialog(e, false);
            }
        }

        private void perform() {
            final MainFrame mainFrame = new MainFrame();

            ServiceFactory.getInstance().getDataDispatcher().addDataHandler(mainFrame);

            // Setup tray
            final RojacTray tray = new RojacTray(mainFrame);
            if (!tray.isSupported()) {
                if (log.isInfoEnabled()) {
                    log.info("Tray is not supported by the system.");
                }
            }

            mainFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowIconified(WindowEvent e) {
                    if (tray.isSupported()) {
                        if (ROJAC_MAIN_FRAME_HIDE_ON_MINIMIZE.get()) {
                            mainFrame.setVisible(false);
                        }
                    }
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    // Hide to tray if such option is set.
                    if (tray.isSupported() && e.getSource() != tray) {
                        if (ROJAC_MAIN_FRAME_HIDE_ON_CLOSE.get()) {
                            mainFrame.setState(Frame.ICONIFIED);
                            mainFrame.setVisible(false);
                            return;
                        }
                    }

                    if (ROJAC_MAIN_FRAME_ASK_ON_CLOSE.get()) {
                        int answer = JLOptionPane.showConfirmDialog(
                                mainFrame,
                                Message.Dialog_ConfirmExit_Message.get(),
                                Message.Dialog_ConfirmExit_Title.get(),
                                JOptionPane.YES_NO_OPTION
                        );

                        if (answer == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }

                    // Remember application layout and other.
                    mainFrame.storeSettings();

                    // Save settings
                    storeSettings();

                    // Close all the resources.
                    ServiceFactory.shutdown();

                    System.exit(0);
                }
            });

            setupUserSettings();

            mainFrame.applySettings();

            mainFrame.setVisible(mainFrame.getExtendedState() != Frame.ICONIFIED || !tray.isSupported());

            mainFrame.setupScheduler();

            new VersionChecker(mainFrame).execute();
        }

    }

}
