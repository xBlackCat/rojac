package org.xblackcat.rojac.service.storage.importing;

import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.connection.DatabaseSettings;
import org.xblackcat.rojac.service.storage.database.connection.SimpleConnectionFactory;

/**
 * 09.04.12 8:58
 *
 * @author xBlackCat
 */
class ImportHelperFactory {
    private ImportHelperFactory() {
    }

    public static IImportHelper createImportHelper(DatabaseSettings settings) throws StorageException {
        SimpleConnectionFactory connectionFactory = new SimpleConnectionFactory(settings);
        // Hardcore class name - prevent ClassNotFoundException
        if ("com.mysql.jdbc.Driver".equals(settings.getJdbcDriverClass().getName())) {
            return new MySqlImportHelper(connectionFactory);
        }

        return new DBImportHelper(connectionFactory);
    }
}
