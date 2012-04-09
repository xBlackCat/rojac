package org.xblackcat.rojac.service.storage.importing;

import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 09.04.12 9:18
 *
 * @author xBlackCat
 */
class MySqlImportHelper extends DBImportHelper {
    public MySqlImportHelper(IConnectionFactory connectionFactory) throws StorageException {
        super(connectionFactory);
    }

    @Override
    protected Statement prepareStatement(Connection con) throws SQLException {
        Statement st = super.prepareStatement(con);
        // Set streaming mode for ResultSet (MySQL only!)
        st.setFetchSize(Integer.MIN_VALUE);
        return st;
    }
}
