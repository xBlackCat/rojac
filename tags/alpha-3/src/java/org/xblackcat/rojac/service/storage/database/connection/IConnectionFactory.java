package org.xblackcat.rojac.service.storage.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author ASUS
 */

public interface IConnectionFactory {
    Connection getWriteConnection() throws SQLException;

    Connection getReadConnection() throws SQLException;

    void shutdown();
}
