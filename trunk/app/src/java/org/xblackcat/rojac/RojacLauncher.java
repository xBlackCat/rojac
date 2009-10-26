package org.xblackcat.rojac;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.gui.dialogs.LoginDialog;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.RojacHelper;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.options.IOptionsService;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.WindowsUtils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
        // Initialize core services
        ServiceFactory.initialize();

        ServiceFactory sf = ServiceFactory.getInstance();

        final IOptionsService optionsService = sf.getOptionsService();
        LookAndFeel laf = optionsService.getProperty(Property.ROJAC_GUI_LOOK_AND_FEEL);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Using LAF: " + laf.getName());
            }
            RojacUtils.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RojacException("Can not initialize " + laf.getName() + " L&F.", e);
        }

        Messages.setLocale(optionsService.getProperty(Property.ROJAC_GUI_LOCALE));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final MainFrame mainFrame = new MainFrame(optionsService);

                mainFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        mainFrame.updateSettings();

                        storeSettings(optionsService);

                        System.exit(0);
                    }
                });

                while (!RojacHelper.isUserRegistered()) {
                    LoginDialog ld = new LoginDialog(mainFrame);
                    WindowsUtils.centerOnScreen(ld);
                    if (ld.showLoginDialog()) {
                        System.exit(0);
                    }
                }

                setupUserSettings();

                mainFrame.applySettings();

                mainFrame.loadData();

                mainFrame.setVisible(true);
            }
        });
    }

    private static void storeSettings(IOptionsService optionsService) {
        if (!RojacHelper.shouldStorePassword()) {
            optionsService.setProperty(Property.RSDN_USER_PASSWORD, null);
        }

        optionsService.storeSettings();
    }

    private static void setupUserSettings() {
        // TODO: Load user settings from options
    }

}
