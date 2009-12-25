package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.ThreadStatData;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* @author xBlackCat
*/
class ToThreadDataConverter implements IToObjectConverter<ThreadStatData> {
@Override
public ThreadStatData convert(ResultSet rs) throws SQLException {
    long lastPostDate = rs.getLong(1);
    int replyAmount = rs.getInt(2);

    return new ThreadStatData(
            lastPostDate,
            replyAmount
    );
}
}