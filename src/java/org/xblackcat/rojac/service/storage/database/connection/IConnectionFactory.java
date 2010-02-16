package org.xblackcat.rojac.service.storage.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author ASUS
 */

public interface IConnectionFactory {
    Connection getConnection() throws SQLException;
}
