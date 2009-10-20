package org.xblackcat.rojac.service.storage.database.convert;

import org.xblackcat.rojac.data.Version;
import org.xblackcat.rojac.data.VersionInfo;
import org.xblackcat.rojac.data.VersionType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ASUS
 */

class ToVersionInfoConverter implements IToObjectConverter<VersionInfo> {
    public VersionInfo convert(ResultSet rs) throws SQLException {
        int type = rs.getInt(1);
        byte[] data = rs.getBytes(2);
        return new VersionInfo(new Version(data), VersionType.getType(type));
    }
}
