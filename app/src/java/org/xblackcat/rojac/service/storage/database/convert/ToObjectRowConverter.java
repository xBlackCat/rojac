package org.xblackcat.rojac.service.storage.database.convert;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 09.04.12 11:45
 *
 * @author xBlackCat
 */
class ToObjectRowConverter implements IToObjectConverter<Object[]> {
    @Override
    public Object[] convert(ResultSet rs) throws SQLException {
        int columnCount = rs.getMetaData().getColumnCount();
        Object[] row = new Object[columnCount];

        for (int i = 0; i < columnCount; i++) {
            row[i] = rs.getObject(i + 1);
        }

        return row;
    }
}
