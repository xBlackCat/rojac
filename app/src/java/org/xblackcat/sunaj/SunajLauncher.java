package org.xblackcat.sunaj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.flexdock.util.SwingUtility;
import org.xblackcat.sunaj.gui.LoginDialog;
import org.xblackcat.sunaj.gui.MainFrame;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.options.IOptionsService;
import org.xblackcat.sunaj.service.options.Property;
import org.xblackcat.sunaj.service.storage.IStorage;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.cached.CachedStorage;
import org.xblackcat.sunaj.service.synchronizer.ISynchronizer;
import org.xblackcat.sunaj.service.synchronizer.SimpleSynchronizer;
import org.xblackcat.sunaj.service.synchronizer.SynchronizationException;
import org.xblackcat.sunaj.util.SunajUtils;

import javax.swing.*;

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
        // Initialize service
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

        MainFrame mainFrame = new MainFrame();
//        testStorage();
        while (os.getProperty(Property.RSDN_USER_NAME) == null ||
                os.getProperty(Property.RSDN_USER_PASSWORD) == null) {
            LoginDialog ld = new LoginDialog(mainFrame);
            SwingUtility.centerOnScreen(ld);
            if (ld.showLoginDialog()) {
                System.exit(0);
            }
        }

        mainFrame.setVisible(true);
        SwingUtility.centerOnScreen(mainFrame);
    }

    private static void testStorage() throws StorageException, SynchronizationException {
        IStorage storage = ServiceFactory.getInstance().getStorage();

        ISynchronizer s = new SimpleSynchronizer("xBlackCat", "tryt0guess", new CachedStorage(storage));

        //s.updateForumList();

        storage.getForumAH().setSubscribeForum(33, true);

        s.synchronize();

        System.exit(0);
    }
}
