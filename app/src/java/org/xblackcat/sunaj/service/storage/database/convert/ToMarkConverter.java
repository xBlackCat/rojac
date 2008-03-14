package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.service.data.Mark;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 3 бер 2008
*
* @author xBlackCat
*/
class ToMarkConverter implements IToObjectConverter<Mark> {
    public Mark convert(ResultSet rs) throws SQLException {
        return Mark.getMark(rs.getInt(1));
    }
}
