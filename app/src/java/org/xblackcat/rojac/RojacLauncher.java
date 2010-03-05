package org.xblackcat.rojac;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.gui.dialogs.ProgressTrackerDialog;
import org.xblackcat.rojac.gui.tray.RojacTray;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.progress.LoggingProgressListener;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.SynchronizationUtils;
import org.xblackcat.rojac.util.UIUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
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

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final MainFrame mainFrame = new MainFrame();

                // Setup tray
                RojacTray tray = new RojacTray(mainFrame);
                if (!tray.isSupported()) {
                    if (log.isInfoEnabled()) {
                        log.info("Tray is not supported by the system.");
                    }
                }

                mainFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
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

                mainFrame.setVisible(true);

                WindowsUtils.center(ptd, mainFrame);

                // Start synchronization
                if (SYNCHRONIZER_SCHEDULE_AT_START.get()) {
                    SynchronizationUtils.startSynchronization(mainFrame);
                }
            }
        });
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

}
