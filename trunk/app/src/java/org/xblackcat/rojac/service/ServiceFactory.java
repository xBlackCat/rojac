package org.xblackcat.rojac.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.service.converter.IMessageParser;
import org.xblackcat.rojac.service.converter.RSDNMessageParserFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.executor.TaskExecutor;
import org.xblackcat.rojac.service.options.IOptionsService;
import org.xblackcat.rojac.service.options.MultiUserOptionsService;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.database.DBStorage;
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

    public static void initialize() throws RojacException {
        INSTANCE = new ServiceFactory();
    }

    private final IExecutor executor;
    private final IStorage storage;
    private final IOptionsService optionsService;
    private final IMessageParser messageParser;

    private ServiceFactory() throws RojacException {
        Properties mainProperties = new Properties();
        try {
            mainProperties.load(ResourceUtils.getResourceAsStream("/config/rojac.config"));
        } catch (IOException e) {
            throw new RojacException("rojac.config was not found in class path", e);
        }

        String home = System.getProperty("rojac.home");
        if (StringUtils.isBlank(home)) {
            String userHome = mainProperties.getProperty("rojac.user.home.def");
            if (StringUtils.isBlank(userHome)) {
                throw new RojacException("Either {$rojac.user.home.def} property in file or {$rojac.home} system property is nod defined.");
            }

            home = ResourceUtils.putSystemProperties(userHome);
            if (log.isDebugEnabled()) {
                log.debug("{$rojac.home} is not defined. It will initialized with '" + home + "' value.");
            }
            System.setProperty("rojac.home", home);
        }

        File homeFolder = new File(home);
        if (!homeFolder.exists()) {
            if (log.isTraceEnabled()) {
                log.trace("Create home folder at " + home);
            }
            homeFolder.mkdirs();
        } else if (!homeFolder.isDirectory()) {
            throw new RojacException("It is impossible to use non-folder resource '" + homeFolder.getAbsolutePath() + "' for storing Rojac configuration.");
        }

        storage = new DBStorage(mainProperties.getProperty("rojac.service.database.engine.config"));
        storage.initialize();

        optionsService = new MultiUserOptionsService();

        try {
            messageParser = new RSDNMessageParserFactory().getParser();
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize message formatter.", e);
        }

        executor = new TaskExecutor();
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

    public IExecutor getExecutor() {
        return executor;
    }

}
