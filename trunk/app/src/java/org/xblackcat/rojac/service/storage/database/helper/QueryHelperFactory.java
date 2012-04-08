package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.storage.StorageInitializationException;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.connection.SimplePooledConnectionFactory;

/**
 * 08.04.12 16:19
 *
 * @author xBlackCat
 */
public class QueryHelperFactory {
    private QueryHelperFactory() {
    }

    public static IQueryHelper createHelper(DatabaseSettings settings) throws StorageInitializationException {
        IConnectionFactory connectionFactory = new SimplePooledConnectionFactory(settings);

        return new QueryHelper(connectionFactory);
    }
}
