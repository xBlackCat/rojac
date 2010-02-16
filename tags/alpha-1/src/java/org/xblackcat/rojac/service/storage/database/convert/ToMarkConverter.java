package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.Mark;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author xBlackCat
 */
class ToMarkConverter implements IToObjectConverter<Mark> {
    public Mark convert(ResultSet rs) throws SQLException {
        return Mark.getMark(rs.getInt(1));
    }
}
