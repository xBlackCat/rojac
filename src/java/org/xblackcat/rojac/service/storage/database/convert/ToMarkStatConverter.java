package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.data.MarkStat;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ASUS
 */

class ToMarkStatConverter implements IToObjectConverter<MarkStat> {
    public MarkStat convert(ResultSet rs) throws SQLException {
        Mark mark = Mark.getMark(rs.getInt(1));
        int amount = rs.getInt(2);
        return new MarkStat(mark, amount);
    }
}
