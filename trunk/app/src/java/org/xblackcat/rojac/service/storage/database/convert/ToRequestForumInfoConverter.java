package org.xblackcat.rojac.service.storage.database.convert;

import ru.rsdn.Janus.RequestForumInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 09.04.12 11:46
 *
 * @author xBlackCat
 */
class ToRequestForumInfoConverter implements IToObjectConverter<RequestForumInfo> {
    @Override
    public RequestForumInfo convert(ResultSet rs) throws SQLException {
        int forumId = rs.getInt(1);
        boolean empty = rs.getBoolean(2);

        return new RequestForumInfo(forumId, empty);
    }
}
