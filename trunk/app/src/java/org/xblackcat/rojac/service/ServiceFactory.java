package org.xblackcat.rojac.service;

import org.apache.commons.lang3.StringUtils;
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
import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.rojac.service.storage.database.DBStorage;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.utils.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        RojacUtils.registerExecutor(executor);

        storage = initializeStorage();

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
            mainProperties = ResourceUtils.loadProperties("/rojac.properties");
        } catch (IOException e) {
            throw new StorageInitializationException("rojac.properties was not found in class path", e);
        }

        String home = System.getProperty("rojac.home");
        if (StringUtils.isBlank(home)) {
            String userHome = mainProperties.getProperty("rojac.home");
            if (StringUtils.isBlank(userHome)) {
                throw new StorageInitializationException("{$rojac.home} is not defined either property in file or system property.");
            }

            home = ResourceUtils.putSystemProperties(userHome);
            if (log.isTraceEnabled()) {
                log.trace("{$rojac.home} is not defined. It will initialized with '" + home + "' value.");
            }
        }

        String dbHome = mainProperties.getProperty("rojac.db.home");
        if (StringUtils.isBlank(dbHome)) {
            if (log.isWarnEnabled()) {
                log.warn("{$rojac.db.home} is not defined. Assumed the same as {$rojac.home}");
            }
            mainProperties.setProperty("rojac.db.home", home);
            dbHome = home;
        }
        dbHome = ResourceUtils.putSystemProperties(dbHome);
        installProperties(mainProperties, "rojac.home", "rojac.db.home", "rojac.db.host", "rojac.db.user", "rojac.db.password");

        checkPath(home);
        checkPath(dbHome);

        String configurationName = mainProperties.getProperty("rojac.database.engine");

        String connectionFactoryName = mainProperties.getProperty("rojac.database.connection_factory");

        IConnectionFactory connectionFactory = createConnectionFactory(connectionFactoryName, configurationName);

        DBStorage storage = new DBStorage(configurationName, connectionFactory);
        storage.initialize();
        return storage;
    }

    /**
     * Register specified properties in system.
     *
     * @param properties
     * @param names
     */
    private static void installProperties(Properties properties, String... names) {
        for (String name : names) {
            String value = properties.getProperty(name);
            if (StringUtils.isBlank(value)) {
                if (log.isTraceEnabled()) {
                    log.trace("Property " + name + " is not defined.");
                }
                continue;
            }

            value = ResourceUtils.putSystemProperties(value);

            if (log.isTraceEnabled()) {
                log.trace("Initialize property " + name + " with value " + value);
            }

            System.setProperty(name, value);
        }
    }

    @SuppressWarnings({"unchecked"})
    private static IConnectionFactory createConnectionFactory(String connectionFactoryName, String configurationName) throws StorageInitializationException {
        Class<?> connectionFactoryClass;
        try {
            connectionFactoryClass = Class.forName("org.xblackcat.rojac.service.storage.database.connection." + connectionFactoryName + "ConnectionFactory");
        } catch (ClassNotFoundException e) {
            try {
                connectionFactoryClass = Class.forName(connectionFactoryName);
            } catch (ClassNotFoundException e1) {
                throw new StorageInitializationException("Connection factory " + connectionFactoryName + " is not found", e1);
            }
        }

        try {
            if (!IConnectionFactory.class.isAssignableFrom(connectionFactoryClass)) {
                throw new StorageInitializationException("Connection factory should implements IConnectionFactory interface.");
            }
            Constructor<IConnectionFactory> connectionFactoryConstructor =
                    ((Class<IConnectionFactory>) connectionFactoryClass).getConstructor(String.class);

            return connectionFactoryConstructor.newInstance(configurationName);
        } catch (NoSuchMethodException e) {
            throw new StorageInitializationException("Connection factory have no necessary constructor", e);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new StorageInitializationException("Can not initialize connection factory", e);
        }
    }

    private static void checkPath(String target) throws RojacException {
        File folder = new File(target);
        if (!folder.exists()) {
            if (log.isTraceEnabled()) {
                log.trace("Create folder at " + target);
            }
            if (!folder.mkdirs()) {
                throw new RojacException("Can not create a '" + folder.getAbsolutePath() + "' folder for storing Rojac configuration.");
            }
        }
        if (!folder.isDirectory()) {
            throw new RojacException("Target path '" + folder.getAbsolutePath() + "' is not a folder.");
        }
    }
}
