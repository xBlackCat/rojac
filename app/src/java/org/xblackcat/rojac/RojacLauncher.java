package org.xblackcat.rojac;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.gui.dialogs.ProgressTrackerDialog;
import org.xblackcat.rojac.gui.tray.RojacTray;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.Messages;
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

public abstract class RojacLauncher {
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

        Messages.setLocale(ROJAC_GUI_LOCALE.get());

        SwingUtilities.invokeLater(new SwingPartInitializer());
    }

    private static void storeSettings() {
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
                                Messages.DIALOG_CONFIRM_EXIT_MESSAGE.get(),
                                Messages.DIALOG_CONFIRM_EXIT_TITLE.get(),
                                JOptionPane.YES_NO_OPTION
                        );

                        if (answer == JOptionPane.NO_OPTION) {
                            return;
                        }
                    }

                    mainFrame.updateSettings();

                    storeSettings();

                    System.exit(0);
                }
            });

            setupUserSettings();

            mainFrame.applySettings();

            mainFrame.loadData();

            // Setup progress dialog.
            ProgressTrackerDialog ptd = new ProgressTrackerDialog(mainFrame);
            ServiceFactory.getInstance()
                    .getProgressControl()
                    .addProgressListener(ptd);

            // Setup scheduled synchronizer
            SynchronizationUtils.setScheduleSynchronizer(mainFrame);

            mainFrame.setVisible(mainFrame.getExtendedState() != Frame.ICONIFIED || !tray.isSupported());

            tray.updateState();

            WindowsUtils.center(ptd, mainFrame);

            // Start synchronization
            if (SYNCHRONIZER_SCHEDULE_AT_START.get()) {
                SynchronizationUtils.startSynchronization(mainFrame);
            }

            new RojacWorker<Void, Integer>() {
                @Override
                protected Void perform() throws Exception {
                    try {
                        Integer lastBuild = RojacUtils.getLastBuild();

                        if (lastBuild != null) {
                            if (log.isDebugEnabled()) {
                                log.debug("Revision on site: " + lastBuild + ". Current revision: " + RojacUtils.REVISION_NUMBER);
                            }

                            if (RojacUtils.REVISION_NUMBER != null && RojacUtils.REVISION_NUMBER < lastBuild) {
                                if (log.isDebugEnabled()) {
                                    log.debug("User should update Rojac dist up to latest version.");
                                }
                                // TODO: show notification dialog about new version on a site.
                            }
                        }
                    } catch (RojacException e) {
                        if (log.isWarnEnabled()) {
                            log.warn("Can not read available last revision from site.", e);
                        }
                        // TODO: show error dialog: can not obtain last version on a site.
                    }

                    return null;
                }
            }.execute();

        }
    }
}
