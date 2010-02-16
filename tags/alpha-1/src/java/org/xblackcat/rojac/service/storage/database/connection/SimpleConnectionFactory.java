package org.xblackcat.rojac.service.storage.database.connection;

import org.xblackcat.rojac.service.storage.StorageInitializationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author ASUS
 */

public class SimpleConnectionFactory extends AConnectionFactory {
    public SimpleConnectionFactory(String configurationName) throws StorageInitializationException {
        super(configurationName);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }
}
