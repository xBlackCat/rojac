package org.xblackcat.sunaj.service.storage.database.convert;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class to convert a single-column result as specified object.
 */

public class ToScalarConvertor<T> implements IToObjectConvertor<T> {
    public T convert(ResultSet rs) throws SQLException {
        return (T) rs.getObject(1);
    }
}