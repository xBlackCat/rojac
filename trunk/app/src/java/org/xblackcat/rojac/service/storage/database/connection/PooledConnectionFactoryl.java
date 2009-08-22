package org.xblackcat.rojac.service.storage.database.connection;

import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.xblackcat.rojac.service.storage.StorageInitializationException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Date: 5 זמגע 2008
 *
 * @author xBlackCat
 */

public class PooledConnectionFactoryl extends AConnectionFactory {
    private final PoolableObjectFactory factory;

    public PooledConnectionFactoryl(String configurationName) throws StorageInitializationException {
        super(configurationName);

        DriverManagerConnectionFactory connectionFactory = new DriverManagerConnectionFactory(url, userName, password);
        factory = new PoolableConnectionFactory(
                connectionFactory,
                new GenericObjectPool(null),
                null,
                "SELECT 1+1",
                false,
                true);
    }

    public Connection getConnection() throws SQLException {
        try {
            return (Connection) factory.makeObject();
        } catch (SQLException e){
            throw e;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }
}
