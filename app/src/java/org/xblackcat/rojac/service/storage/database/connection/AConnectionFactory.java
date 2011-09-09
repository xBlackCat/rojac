package org.xblackcat.rojac.service.storage.database.connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.StorageInitializationException;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */

public abstract class AConnectionFactory implements IConnectionFactory {
    protected final Log log = LogFactory.getLog(getClass());

    protected final DatabaseSettings databaseSettings;

    AConnectionFactory(DatabaseSettings databaseSettings) throws StorageInitializationException {
        this.databaseSettings = databaseSettings;
    }

    @Override
    public void shutdown() {
        if (databaseSettings.getShutdownUrl() != null) {
            try {
                DriverManager.getConnection(databaseSettings.getShutdownUrl());
            } catch (SQLException e) {
                log.error("Can not execute shutdown sequence in DB.", e);
            }
        }
    }
}
