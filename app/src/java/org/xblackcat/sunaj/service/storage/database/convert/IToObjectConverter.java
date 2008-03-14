package org.xblackcat.sunaj.service.storage.database.convert;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 17.04.2007
 *
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
