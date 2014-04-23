package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.sjpu.storage.converter.IToObjectConverter;
import ru.rsdn.janus.RequestForumInfo;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 09.04.12 11:46
 *
 * @author xBlackCat
 */
public class ToRequestForumInfoConverter implements IToObjectConverter<RequestForumInfo> {
    @Override
    public RequestForumInfo convert(ResultSet rs) throws SQLException {
        int forumId = rs.getInt(1);
        boolean empty = rs.getBoolean(2);

        RequestForumInfo info = new RequestForumInfo();
        info.setForumId(forumId);
        info.setIsFirstRequest(empty);
        return info;
    }
}
