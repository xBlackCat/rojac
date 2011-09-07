package org.xblackcat.rojac.service.storage.database.connection;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.xblackcat.rojac.service.storage.StorageInitializationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */

public class SimplePooledConnectionFactory extends AConnectionFactory {
    private final ObjectPool connectionPool = new GenericObjectPool(null, 20);

    public SimplePooledConnectionFactory(ISettings settings) throws StorageInitializationException {
        super(settings);

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                settings.getUrl(),
                settings.getUserName(),
                settings.getPassword()
        );

        new PoolableConnectionFactory(
                connectionFactory,
                connectionPool,
                null,
                "SELECT 1+1",
                false,
                true);

        try {
            Class.forName("org.apache.commons.dbcp.PoolingDriver");
        } catch (ClassNotFoundException e) {
            throw new StorageInitializationException("Can not initialize pooling driver", e);
        }

        try {
            PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");

            driver.registerPool("rojacdb", connectionPool);
        } catch (SQLException e) {
            throw new StorageInitializationException("Can not obtain pooling driver", e);
        }
    }

    public Connection getWriteConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:apache:commons:dbcp:rojacdb");
    }

    @Override
    public Connection getReadConnection() throws SQLException {
        return getWriteConnection();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        try {
            PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
            driver.closePool("rojacdb");
        } catch (SQLException e) {
            log.error("Can not close pool.", e);
        }
    }
}
