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
    private final ObjectPool connectionPool = new GenericObjectPool(null, 20, GenericObjectPool.WHEN_EXHAUSTED_GROW, 0, true, true);
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

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionUrl);
    }

    @Override
    public void shutdown() {
        super.shutdown();
        // TODO: enable and find another way to kill tasks waiting for connections.
//        try {
//            driver.closePool(poolName);
//        } catch (SQLException e) {
//            log.error("Can not close pool.", e);
//        }
    }
}
