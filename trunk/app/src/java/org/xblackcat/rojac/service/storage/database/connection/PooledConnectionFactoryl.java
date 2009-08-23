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
 * Date: 5 זמגע 2008
 *
 * @author xBlackCat
 */

public class PooledConnectionFactoryl extends AConnectionFactory {
    public PooledConnectionFactoryl(String configurationName) throws StorageInitializationException {
        super(configurationName);

        ObjectPool connectionPool = new GenericObjectPool(null);

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, userName, password);

        
         new PoolableConnectionFactory(
                connectionFactory,
                connectionPool,
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

        driver.registerPool("rojacdb", connectionPool);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:apache:commons:dbcp:rojacdb");
    }
}
