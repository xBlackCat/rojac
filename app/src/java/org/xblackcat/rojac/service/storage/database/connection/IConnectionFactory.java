package org.xblackcat.rojac.service.storage.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public interface IConnectionFactory {
    Connection getConnection() throws SQLException;
}
