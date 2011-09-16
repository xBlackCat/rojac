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
    private final String poolName;
    private final String connectionUrl;
    private final PoolingDriver driver;

    public SimplePooledConnectionFactory(DatabaseSettings databaseSettings) throws StorageInitializationException {
        super(databaseSettings);
        poolName = "rojacdb." + System.currentTimeMillis();

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                databaseSettings.getUrl(),
                databaseSettings.getUserName(),
                databaseSettings.getPassword()
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
            driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");

            driver.registerPool(poolName, connectionPool);
        } catch (SQLException e) {
            throw new StorageInitializationException("Can not obtain pooling driver", e);
        }
        connectionUrl = "jdbc:apache:commons:dbcp:" + poolName;
    }

    public Connection getWriteConnection() throws SQLException {
        try {
            driver.getConnectionPool(poolName);
        } catch (SQLException e) {
            return new FakeConnection();
        }
        return DriverManager.getConnection(connectionUrl);
    }

    @Override
    public Connection getReadConnection() throws SQLException {
        return getWriteConnection();
    }

    @Override
    public void shutdown() {
        super.shutdown();
        try {
            driver.closePool(poolName);
        } catch (SQLException e) {
            log.error("Can not close pool.", e);
        }
    }
}
