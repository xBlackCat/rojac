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

public class RWPooledConnectionFactory extends AConnectionFactory {
    private final ObjectPool writeConnectionPool = new GenericObjectPool(null, 1);
    private final ObjectPool readConnectionPool = new GenericObjectPool(null, 20);

    public RWPooledConnectionFactory(DatabaseSettings databaseSettings) throws StorageInitializationException {
        super(databaseSettings);

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                databaseSettings.getUrl(),
                databaseSettings.getUserName(),
                databaseSettings.getPassword()
        );

        new PoolableConnectionFactory(
                connectionFactory,
                writeConnectionPool,
                null,
                "SELECT 1+1",
                false,
                true);

        new PoolableConnectionFactory(
                connectionFactory,
                readConnectionPool,
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

            driver.registerPool("rojacdb_write", writeConnectionPool);
            driver.registerPool("rojacdb_read", readConnectionPool);
        } catch (SQLException e) {
            throw new StorageInitializationException("Can not obtain pooling driver", e);
        }
    }

    public Connection getWriteConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:apache:commons:dbcp:rojacdb_write");
    }

    @Override
    public Connection getReadConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:apache:commons:dbcp:rojacdb_read");
    }

    @Override
    public void shutdown() {
        super.shutdown();
        try {
            PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
            driver.closePool("rojacdb_write");
            driver.closePool("rojacdb_read");
        } catch (SQLException e) {
            log.error("Can not close pools.", e);
        }
    }
}
