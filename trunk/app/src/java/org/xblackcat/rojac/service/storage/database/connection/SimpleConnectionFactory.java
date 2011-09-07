package org.xblackcat.rojac.service.storage.database.connection;

import org.xblackcat.rojac.service.storage.StorageInitializationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author ASUS
 */

public class SimpleConnectionFactory extends AConnectionFactory {
    public SimpleConnectionFactory(ISettings settings) throws StorageInitializationException {
        super(settings);
    }

    public Connection getWriteConnection() throws SQLException {
        return DriverManager.getConnection(settings.getUrl(), settings.getUserName(), settings.getPassword());
    }

    @Override
    public Connection getReadConnection() throws SQLException {
        return getWriteConnection();
    }
}
