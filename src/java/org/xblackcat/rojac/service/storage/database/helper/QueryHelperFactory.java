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

    public static IManagingQueryHelper createHelper(DatabaseSettings settings) throws StorageInitializationException {
        IConnectionFactory connectionFactory = new SimplePooledConnectionFactory(settings);

        IResultFactory resultFactory;
        if ("com.mysql.jdbc.Driver".equals(settings.getJdbcDriverClass().getName())) {
            resultFactory = new MySqlStreamingResultFactory();
        } else {
            resultFactory = new StreamingResultFactory();
        }

        return new ManagingQueryHelper(connectionFactory, new StreamingDataFetcher(resultFactory));
    }
}
