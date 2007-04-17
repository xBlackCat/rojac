package org.xblackcat.sunaj.service.storage.database.convert;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public class ToBooleanConvertor implements IToObjectConvertor<Boolean> {
    public Boolean convert(ResultSet rs) throws SQLException {
        return rs.getBoolean(1);
    }
}
