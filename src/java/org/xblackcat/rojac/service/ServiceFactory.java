package org.xblackcat.rojac.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.service.converter.IMessageParser;
import org.xblackcat.rojac.service.converter.RSDNMessageParserFactory;
import org.xblackcat.rojac.service.executor.IExecutor;
import org.xblackcat.rojac.service.executor.TaskExecutor;
import org.xblackcat.rojac.service.progress.IProgressController;
import org.xblackcat.rojac.service.progress.ProgressController;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.sjpu.storage.StorageException;

import java.io.IOException;

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
            Storage.shutdown();
        } catch (StorageException e) {
            log.error("Failed to shutdown storage.", e);
        }
    }

    private final IExecutor executor;
    private final IMessageParser messageParser;
    private final IProgressController progressController;

    private ServiceFactory() throws RojacException {
        progressController = new ProgressController();

        executor = new TaskExecutor();
        RojacUtils.registerExecutor(executor);


        try {
            messageParser = new RSDNMessageParserFactory().getParser();
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize message formatter.", e);
        }

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

}
