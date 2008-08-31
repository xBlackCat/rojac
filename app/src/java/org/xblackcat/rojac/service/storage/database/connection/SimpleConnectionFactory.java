package org.xblackcat.rojac.service.storage.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public class SimpleConnectionFactory implements IConnectionFactory {
    private final String url;
    private final String userName;
    private final String password;

    public SimpleConnectionFactory(String url, String userName, String password) {
        this.url = url;
        this.userName = userName;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, password);
    }
}
