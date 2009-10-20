package org.xblackcat.rojac.service.storage.database.convert;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ASUS
 */

public interface IToObjectConverter<T> {
    /**
     * Converts
     *
     * @param rs
     *
     * @return
     */
    T convert(ResultSet rs) throws SQLException;
}
