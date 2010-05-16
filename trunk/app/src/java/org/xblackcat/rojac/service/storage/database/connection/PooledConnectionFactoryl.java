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

public class PooledConnectionFactoryl extends AConnectionFactory {
    public PooledConnectionFactoryl(String configurationName) throws StorageInitializationException {
        super(configurationName);

        ObjectPool writeConnectionPool = new GenericObjectPool(null, 1);
        ObjectPool readConnectionPool = new GenericObjectPool(null, 10);

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, userName, password);

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

        PoolingDriver driver;
        try {
            Class.forName("org.apache.commons.dbcp.PoolingDriver");
            driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
        } catch (ClassNotFoundException e) {
            throw new StorageInitializationException("Can not initialize pooling driver", e);
        } catch (SQLException e) {
            throw new StorageInitializationException("Can not obtain pooling driver", e);
        }

        driver.registerPool("rojacdb_write", writeConnectionPool);
        driver.registerPool("rojacdb_read", readConnectionPool);
    }

    public Connection getWriteConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:apache:commons:dbcp:rojacdb_write");
    }

    @Override
    public Connection getReadConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:apache:commons:dbcp:rojacdb_read");
    }
}
