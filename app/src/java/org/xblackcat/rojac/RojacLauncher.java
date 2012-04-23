package org.xblackcat.rojac;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.gui.component.Windows7Bar;
import org.xblackcat.rojac.gui.tray.RojacTray;
import org.xblackcat.rojac.i18n.JLOptionPane;
import org.xblackcat.rojac.i18n.LocaleControl;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.APacket;
import org.xblackcat.rojac.service.datahandler.DataDispatcher;
import org.xblackcat.rojac.service.options.MultiUserOptionsService;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.progress.LoggingProgressListener;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.StorageInstaller;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.util.*;
import org.xblackcat.schema.data.DataStreamHandlerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import static org.xblackcat.rojac.service.options.Property.*;

/**
 * @author Alexey
 */

public final class RojacLauncher {
    private static final Log log = LogFactory.getLog(RojacLauncher.class);

    private RojacLauncher() {
    }

    public static void main(String... args) {
        URL.setURLStreamHandlerFactory(new DataStreamHandlerFactory());

        try {
            launch(args);
        } catch (Exception e) {
            log.fatal("Can not initialize Rojac", e);
        }
    }

    private static void launch(String[] args) throws Exception {
        if (SWTUtils.isSwtEnabled) {
            chrriis.common.UIUtils.setPreferredLookAndFeel();
            NativeInterface.open();
        }

        Thread.setDefaultUncaughtExceptionHandler(RojacUtils.GLOBAL_EXCEPTION_HANDLER);

        // Load and install core Rojac settings
        RojacUtils.loadCoreOptions();

        // For the first - load program options
        Property.setOptionsService(new MultiUserOptionsService());

        RunChecker checker = new RunChecker();
        Integer postId = null;
        if (!ArrayUtils.isEmpty(args)) {
            try {
                postId = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                postId = LinkUtils.getMessageIdFromUrl(args[0]);
            }
        }

        Boolean shutdownOthers = Property.ROJAC_DEBUG_SHUTDOWN_OTHER.get();
        if (checker.performCheck(shutdownOthers, postId)) {
            return;
        }

        // Initialize and install packet dispatcher service
        APacket.setDispatcher(new DataDispatcher());

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

        SwingUtilities.invokeLater(new SwingPartInitializer(checker, postId));

        if (SWTUtils.isSwtEnabled) {
            NativeInterface.runEventPump();
        }
    }

    private static void performShutdown(MainFrame mainFrame) {
        // Remember application layout and other.
        mainFrame.storeSettings();

        // Save settings
        RojacUtils.storeSettings();

        try {
            Storage.shutdown();
        } catch (StorageException e) {
            log.error("Can not shutdown database", e);
        }

        // Close all the resources.
        ServiceFactory.shutdown();
    }

    private static class SwingPartInitializer implements Runnable {
        private final RunChecker checker;
        private final Integer postId;

        public SwingPartInitializer(RunChecker checker, Integer postId) {
            this.checker = checker;
            this.postId = postId;
        }

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

            final Runnable shutDownAction = new Runnable() {
                @Override
                public void run() {
                    performShutdown(mainFrame);
                }
            };

            new VersionChecker(mainFrame).execute();

            checker.installNewInstanceListener(
                    // Show window
                    new IRemoteAction() {
                        public void run(Integer postId) {
                            WindowsUtils.toFront(mainFrame);

                            if (postId != null) {
                                mainFrame.openMessage(postId, Property.OPEN_MESSAGE_BEHAVIOUR_GENERAL.get());
                            }
                        }
                    },
                    // Shutdown
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                SwingUtilities.invokeAndWait(shutDownAction);
                            } catch (InterruptedException e) {
                                log.error("Shutdown process is interrupted", e);
                            } catch (InvocationTargetException e) {
                                log.error("Got exception from shutdown script", e);
                            }
                        }
                    }
            );

            APacket.getDispatcher().addDataHandler(mainFrame);

            if (Windows7Bar.isSupported()) {
                Windows7Bar taskBar = Windows7Bar.getWindowBar(mainFrame);

                ServiceFactory.getInstance()
                        .getProgressControl()
                        .addProgressListener(new Windows7BarProgressTracker(mainFrame, taskBar));
            }

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
                    shutDownAction.run();

                    System.exit(0);
                }
            });

            if (!Property.ROJAC_DEBUG_DONT_RESTORE_LAYOUT.get()) {
                mainFrame.applySettings();
            }

            DatabaseSettings settings = ROJAC_DATABASE_CONNECTION_SETTINGS.get();
            boolean visible = mainFrame.getExtendedState() != Frame.ICONIFIED || !tray.isSupported() || settings == null;
            if (visible) {
                WindowsUtils.toFront(mainFrame);
            }

            if (SWTUtils.isSwtEnabled) {
                mainFrame.installBrowser(SWTUtils.getBrowser());
            }

            new StorageInstaller(
                    new Runnable() {
                        @Override
                        public void run() {
                            mainFrame.setupScheduler();

                            if (postId != null) {
                                mainFrame.openMessage(postId, Property.OPEN_MESSAGE_BEHAVIOUR_GENERAL.get());
                            }
                        }
                    },
                    shutDownAction,
                    settings,
                    mainFrame
            ).execute();
        }

    }
}
