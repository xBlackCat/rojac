package org.xblackcat.sunaj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.gui.MainFrame;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.options.Property;
import org.xblackcat.sunaj.service.storage.IStorage;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.cached.CachedStorage;
import org.xblackcat.sunaj.service.synchronizer.ISynchronizer;
import org.xblackcat.sunaj.service.synchronizer.SimpleSynchronizer;
import org.xblackcat.sunaj.service.synchronizer.SynchronizationException;
import org.xblackcat.sunaj.util.SunajUtils;
import org.xblackcat.sunaj.util.WindowsUtils;

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

        LookAndFeel laf = sf.getOptionsService().getProperty(Property.SUNAJ_GUI_LOOK_AND_FEEL);
        try {
            SunajUtils.setLookAndFeel(laf);
        } catch (UnsupportedLookAndFeelException e) {
            throw new SunajException("Can not initialize " + laf.getName() + " L&F.", e);
        }

        testStorage();

        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        WindowsUtils.moveToScreenCenter(mainFrame);
    }

    private static void testStorage() throws StorageException, SynchronizationException {
        IStorage storage = ServiceFactory.getInstance().getStorage();
        storage.initialize();

        ISynchronizer s = new SimpleSynchronizer("xBlackCat", "tryt0guess", new CachedStorage(storage));

        //s.updateForumList();

        storage.getForumAH().setSubscribeForum(33, true);

        s.synchronize();

        System.exit(0);
    }
}
