package org.xblackcat.sunaj;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.gui.MainFrame;
import org.xblackcat.sunaj.service.ServiceFactory;
import org.xblackcat.sunaj.service.storage.IStorage;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.cached.CachedStorage;
import org.xblackcat.sunaj.service.synchronizer.ISynchronizer;
import org.xblackcat.sunaj.service.synchronizer.ISynchronizerFactory;
import org.xblackcat.sunaj.service.synchronizer.SimpleSynchronizerFactory;
import org.xblackcat.sunaj.service.synchronizer.SynchronizationException;
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
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            log.error("Can not initialize Windows L&F. The default L&F will be used", e);
        }

        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        WindowsUtils.moveToScreenCenter(mainFrame);
    }

    private static void testStorage() throws StorageException, SynchronizationException {
        IStorage storage = ServiceFactory.getInstance().getStorage();
        boolean b = storage.checkStructure();
        if (!b) {
            storage.initialize();
        }

        ISynchronizerFactory sf = new SimpleSynchronizerFactory("xBlackCat", "tryt0guess", new CachedStorage(storage));

        ISynchronizer s = sf.getSynchronizer();

        s.synchronize();
    }
}
