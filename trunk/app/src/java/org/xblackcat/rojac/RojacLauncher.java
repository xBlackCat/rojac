package org.xblackcat.rojac;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flexdock.util.SwingUtility;
import org.xblackcat.rojac.gui.LoginDialog;
import org.xblackcat.rojac.gui.MainFrame;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.options.IOptionsService;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Date: 26 ��� 2007
 *
 * @author Alexey
 */

public final class RojacLauncher {
    private static final Log log = LogFactory.getLog(RojacLauncher.class);

    private RojacLauncher() {
    }

    public static void main(String[] args) throws Exception {
        // Initialize core services
        ServiceFactory.initialize();

        ServiceFactory sf = ServiceFactory.getInstance();

        IOptionsService os = sf.getOptionsService();
        LookAndFeel laf = os.getProperty(Property.ROJAC_GUI_LOOK_AND_FEEL);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Using LAF: " + laf.getName());
            }
            RojacUtils.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RojacException("Can not initialize " + laf.getName() + " L&F.", e);
        }

        Messages.setLocale(os.getProperty(Property.ROJAC_GUI_LOCALE));

        final MainFrame mainFrame = new MainFrame();

        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainFrame.updateSettings();

                storeSettings();

                System.exit(0);
            }
        });

        while (os.getProperty(Property.RSDN_USER_NAME) == null ||
                os.getProperty(Property.RSDN_USER_PASSWORD) == null) {
            LoginDialog ld = new LoginDialog(mainFrame);
            SwingUtility.centerOnScreen(ld);
            if (ld.showLoginDialog()) {
                System.exit(0);
            }
        }

        setupUserSettings();

        mainFrame.applySettings();

        mainFrame.loadData();

        mainFrame.setVisible(true);
    }

    private static void storeSettings() {
        IOptionsService os = ServiceFactory.getInstance().getOptionsService();

        if (!os.getProperty(Property.RSDN_USER_PASSWORD_SAVE)) {
            os.setProperty(Property.RSDN_USER_PASSWORD, null);
        }

        os.storeSettings();
    }

    private static void setupUserSettings() {
        // TODO: Load user settings from options
    }

}