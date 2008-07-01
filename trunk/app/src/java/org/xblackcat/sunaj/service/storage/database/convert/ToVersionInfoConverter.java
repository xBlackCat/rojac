package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.data.Version;
import org.xblackcat.sunaj.data.VersionInfo;
import org.xblackcat.sunaj.data.VersionType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 ��� 2007
 *
 * @author ASUS
 */

class ToVersionInfoConverter implements IToObjectConverter<VersionInfo> {
    public VersionInfo convert(ResultSet rs) throws SQLException {
        int type = rs.getInt(1);
        byte[] data = rs.getBytes(2);
        return new VersionInfo(new Version(data), VersionType.getType(type));
    }
}