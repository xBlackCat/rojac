package org.xblackcat.sunaj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flexdock.util.SwingUtility;
import org.xblackcat.sunaj.gui.LoginDialog;
import org.xblackcat.sunaj.gui.MainFrame;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.options.IOptionsService;
import org.xblackcat.sunaj.service.options.Property;
import org.xblackcat.sunaj.util.SunajUtils;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Date: 26 бер 2007
 *
 * @author Alexey
 */

public final class SunajLauncher {
    private static final Log log = LogFactory.getLog(SunajLauncher.class);

    private SunajLauncher() {
    }

    public static void main(String[] args) throws Exception {
        // Initialize core services
        ServiceFactory sf = ServiceFactory.getInstance();

        IOptionsService os = sf.getOptionsService();
        LookAndFeel laf = os.getProperty(Property.SUNAJ_GUI_LOOK_AND_FEEL);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Using LAF: " + laf.getName());
            }
            SunajUtils.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) {
            throw new SunajException("Can not initialize " + laf.getName() + " L&F.", e);
        }

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
