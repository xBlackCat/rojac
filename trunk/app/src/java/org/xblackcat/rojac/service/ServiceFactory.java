package org.xblackcat.rojac.service;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.service.converter.IMessageParser;
import org.xblackcat.rojac.service.converter.RSDNMessageParserFactory;
import org.xblackcat.rojac.service.datahandler.DataDispatcher;
import org.xblackcat.rojac.service.datahandler.IDataDispatcher;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.executor.TaskExecutor;
import org.xblackcat.rojac.service.options.IOptionsService;
import org.xblackcat.rojac.service.options.MultiUserOptionsService;
import org.xblackcat.rojac.service.progress.IProgressController;
import org.xblackcat.rojac.service.progress.ProgressController;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.DBStorage;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.connection.PooledConnectionFactory;
import org.xblackcat.utils.ResourceUtils;
import sun.awt.AppContext;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
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


    public static void shutdown() {
        INSTANCE.getExecutor().shutdownNow();
        try {
            INSTANCE.getStorage().shutdown();
        } catch (StorageException e) {
            log.error("Failed to shutdown storage.", e);
        }
    }

    public static void initializeTest(IExecutor executor, IStorage storage, IOptionsService optionsService, IMessageParser messageParser, IProgressController progressController, IDataDispatcher dataDispatcher) {
        INSTANCE = new ServiceFactory(executor, storage, optionsService, messageParser, progressController, dataDispatcher);
    }

    private final IExecutor executor;
    private final IStorage storage;
    private final IOptionsService optionsService;
    private final IMessageParser messageParser;
    private final IProgressController progressController;
    private final IDataDispatcher dataDispatcher;

    private ServiceFactory() throws RojacException {
        progressController = new ProgressController();
        dataDispatcher = new DataDispatcher();

        executor = new TaskExecutor();
        final AppContext appContext = AppContext.getAppContext();
        appContext.put(SwingWorker.class, executor);

        storage = initializeStorage();

        optionsService = new MultiUserOptionsService();

        try {
            messageParser = new RSDNMessageParserFactory().getParser();
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize message formatter.", e);
        }
    }

    /**
     * For testing purposes.
     *
     * @param executor
     * @param storage
     * @param optionsService
     * @param messageParser
     * @param progressController
     * @param dataDispatcher
     */
    private ServiceFactory(IExecutor executor, IStorage storage, IOptionsService optionsService, IMessageParser messageParser, IProgressController progressController, IDataDispatcher dataDispatcher) {
        this.executor = executor;
        this.storage = storage;
        this.optionsService = optionsService;
        this.messageParser = messageParser;
        this.progressController = progressController;
        this.dataDispatcher = dataDispatcher;
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

    public IProgressController getProgressControl() {
        return progressController;
    }

    public IDataDispatcher getDataDispatcher() {
        return dataDispatcher;
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
        dbHome = ResourceUtils.putSystemProperties(dbHome);
        System.setProperty("rojac.db.home", dbHome);

        checkPath(home);
        checkPath(dbHome);

        String configurationName = mainProperties.getProperty("rojac.database.engine");

        IConnectionFactory connectionFactory = new PooledConnectionFactory(configurationName);
        DBStorage storage = new DBStorage(configurationName, connectionFactory);
        storage.initialize();
        return storage;
    }

    private static void checkPath(String target) throws RojacException {
        File folder = new File(target);
        if (!folder.exists()) {
            if (log.isTraceEnabled()) {
                log.trace("Create folder at " + target);
            }
            folder.mkdirs();
        }
        if (!folder.isDirectory()) {
            throw new RojacException("Can not create a '" + folder.getAbsolutePath() + "' folder for storing Rojac configuration.");
        }
    }
}
