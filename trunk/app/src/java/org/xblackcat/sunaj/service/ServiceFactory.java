package org.xblackcat.sunaj.service;

import org.xblackcat.sunaj.service.converter.IMessageParser;
import org.xblackcat.sunaj.service.converter.RSDNMessageParserFactory;
import org.xblackcat.sunaj.service.options.IOptionsService;
import org.xblackcat.sunaj.service.options.MultiUserOptionsService;
import org.xblackcat.sunaj.service.storage.IStorage;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.DBStorage;

import java.io.IOException;

/**
 * Date: 29 ñ³÷ 2008
 *
 * @author xBlackCat
 */

public final class ServiceFactory {
    private static final ServiceFactory INSTANCE = new ServiceFactory();

    public static ServiceFactory getInstance() {
        return INSTANCE;
    }

    private final IStorage storage;
    private final IOptionsService optionsService;
    private final IMessageParser messageParser;

    private ServiceFactory() {
        try {
            storage = new DBStorage("dbconfig/smallsql");
        } catch (StorageException e) {
            throw new RuntimeException("Can't initialize database storage engine.", e);
        }
        optionsService = new MultiUserOptionsService();

        try {
            messageParser = new RSDNMessageParserFactory().getParser();
        } catch (IOException e) {
            throw new RuntimeException("Can't initialize converter.", e);
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
