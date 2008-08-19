package org.xblackcat.sunaj.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.sunaj.SunajException;
import org.xblackcat.sunaj.service.converter.IMessageParser;
import org.xblackcat.sunaj.service.converter.RSDNMessageParserFactory;
import org.xblackcat.sunaj.service.options.IOptionsService;
import org.xblackcat.sunaj.service.options.MultiUserOptionsService;
import org.xblackcat.sunaj.service.storage.IStorage;
import org.xblackcat.sunaj.service.storage.database.DBStorage;
import org.xblackcat.utils.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * Date: 29 ñ³÷ 2008
 *
 * @author xBlackCat
 */

public final class ServiceFactory {
    private static final Log log = LogFactory.getLog(ServiceFactory.class);

    private static ServiceFactory INSTANCE = null;

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    public static void initialize() throws SunajException {
        INSTANCE = new ServiceFactory();
    }

    private final IStorage storage;
    private final IOptionsService optionsService;
    private final IMessageParser messageParser;

    private ServiceFactory() throws SunajException {
        Properties mainProperties = new Properties();
        try {
            mainProperties.load(ResourceUtils.getResourceAsStream("/config/sunaj.config"));
        } catch (IOException e) {
            throw new SunajException("sunaj.config was not found in class path", e);
        }

        String home = System.getProperty("sunaj.home");
        if (StringUtils.isBlank(home)) {
            String userHome = mainProperties.getProperty("sunaj.user.home.def");
            if (StringUtils.isBlank(userHome)) {
                throw new SunajException("Either {$sunaj.user.home.def} property in file or {$sunaj.home} system property is nod defined.");
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
            throw new SunajException("It is impossible to use non-folder resource '" + homeFolder.getAbsolutePath() + "' for storing Sunaj configuration.");
        }

        storage = new DBStorage(mainProperties.getProperty("sunaj.service.database.engine.config"));
        storage.initialize();

        optionsService = new MultiUserOptionsService();

        try {
            messageParser = new RSDNMessageParserFactory().getParser();
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize message formatter.", e);
        }
    }

    public IStorage getStorage() {
        return storage;
    }

    public IOptionsService getOptionsService() {
        return optionsService;
    }

    public IMessageParser getMessageConverter() {
        return messageParser;
    }
}
