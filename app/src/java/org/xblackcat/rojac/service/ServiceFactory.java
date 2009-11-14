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
import org.xblackcat.rojac.service.progress.IProgressControl;
import org.xblackcat.rojac.service.progress.ProgressControl;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.database.DBStorage;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.connection.PooledConnectionFactoryl;
import org.xblackcat.utils.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author xBlackCat
 */

public final class ServiceFactory {
    private static final Log log = LogFactory.getLog(ServiceFactory.class);

    private static ServiceFactory INSTANCE = null;
    private static final String DBCONFIG_PACKAGE = "dbconfig/";

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
    private final IProgressControl progressControl;

    private ServiceFactory() throws RojacException {
        storage = initializeStorage();

        optionsService = new MultiUserOptionsService();

        try {
            messageParser = new RSDNMessageParserFactory().getParser();
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize message formatter.", e);
        }

        progressControl = new ProgressControl();

        executor = new TaskExecutor(progressControl);
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

    public IProgressControl getProgressControl() {
        return progressControl;
    }

    private static DBStorage initializeStorage() throws RojacException {
        Properties mainProperties;
        try {
            mainProperties = ResourceUtils.loadProperties("/rojac.config");
        } catch (IOException e) {
            throw new RojacException("rojac.config was not found in class path", e);
        }

        String home = System.getProperty("rojac.home");
        if (StringUtils.isBlank(home)) {
            String userHome = mainProperties.getProperty("rojac.home");
            if (StringUtils.isBlank(userHome)) {
                throw new RojacException("{$rojac.home} is not defined either property in file or system property.");
            }

            home = ResourceUtils.putSystemProperties(userHome);
            if (log.isTraceEnabled()) {
                log.trace("{$rojac.home} is not defined. It will initialized with '" + home + "' value.");
            }
            System.setProperty("rojac.home", home);
        }

        String dbHome = mainProperties.getProperty("rojac.db.home");
        if (StringUtils.isBlank(dbHome)) {
            if (log.isWarnEnabled()) {
                log.warn("{$rojac.db.home} is not defined. Assumed the same as {$rojac.home}");
            }
            dbHome = home;
        }
        System.setProperty("rojac.db.home", ResourceUtils.putSystemProperties(dbHome));

        File homeFolder = new File(home);
        if (!homeFolder.exists()) {
            if (log.isTraceEnabled()) {
                log.trace("Create home folder at " + home);
            }
            homeFolder.mkdirs();
        }
        if (!homeFolder.isDirectory()) {
            throw new RojacException("Can not create a '" + homeFolder.getAbsolutePath() + "' folder for storing Rojac configuration.");
        }

        String configurationName = DBCONFIG_PACKAGE + mainProperties.getProperty("rojac.database.engine");

        IConnectionFactory connectionFactory = new PooledConnectionFactoryl(configurationName);
        DBStorage storage = new DBStorage(configurationName, connectionFactory);
        storage.initialize();
        return storage;
    }
}
