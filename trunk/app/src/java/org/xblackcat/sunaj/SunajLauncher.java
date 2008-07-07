package org.xblackcat.sunaj;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.apache.commons.lang.StringUtils;
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
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.io.File;
import java.util.Properties;

/**
 * Date: 26 бер 2007
 *
 * @author Alexey
 */

public final class SunajLauncher {
    private static final Log log = LogFactory.getLog(SunajLauncher.class);

    private SunajLauncher() {
    }

    private static void initialization() throws Exception {
        Properties mainProperties = new Properties();
        mainProperties.load(ResourceUtils.getResourceAsStream("sunaj.properties"));

        if (log.isDebugEnabled()) {
            log.debug("Perform disk initialization");
        }
        String home = System.getProperty("sunaj.home");
        if (StringUtils.isBlank(home)) {
            String userHome = mainProperties.getProperty("sunaj.user.home.def");
            if (StringUtils.isBlank(userHome)) {
                log.error("Either {$sunaj.user.home.def} property in file and {$sunaj.home} sustem properties are nod defined!. Program will exit.");
                System.exit(1);
            }

            home = ResourceUtils.putSystemProperties(userHome);
            if (log.isDebugEnabled()) {
                log.debug("{$sunaj.home} is not defined. It will initialized with '" + home + "' value.");
            }
            System.setProperty("sunaj.home", home);
        }

        File homeFolder = new File(home);
        if (!homeFolder.exists()) {
            if (log.isTraceEnabled()) {
                log.trace("Create home folder at " + home);
            }
            homeFolder.mkdirs();
        } else if (!homeFolder.isDirectory()) {
            log.error("It is impossible to use non-folder resource for storing Sunaj configuration.");
            System.exit(2);
        }

        // Gui initialization
        if (log.isDebugEnabled()) {
            log.debug("Perform GUI initialization.");
        }
        try {
            UIManager.setLookAndFeel(new WindowsLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            log.error("Can not initialize Windows L&F. The default L&F will be used", e);
        }
    }

    public static void main(String[] args) throws Exception {
        initialization();

        testStorage();

        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
        WindowsUtils.moveToScreenCenter(mainFrame);
    }

    private static void testStorage() throws StorageException, SynchronizationException {
        IStorage storage = ServiceFactory.getInstance().getStorage();
        storage.initialize();

        ISynchronizerFactory sf = new SimpleSynchronizerFactory("xBlackCat", "tryt0guess", new CachedStorage(storage));

        ISynchronizer s = sf.getSynchronizer();

        //s.updateForumList();

        storage.getForumAH().setSubscribeForum(33, true);

        s.synchronize();

        System.exit(0);
    }
}
