package org.xblackcat.sunaj.service.storage.database.convert;

import org.xblackcat.sunaj.service.data.Version;
import org.xblackcat.sunaj.service.data.VersionInfo;
import org.xblackcat.sunaj.service.data.VersionType;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 26 квіт 2007
 *
 * @author ASUS
 */

class ToVersionInfoConverter implements IToObjectConverter<VersionInfo> {
    public VersionInfo convert(ResultSet rs) throws SQLException {
        int type = rs.getInt(1);
        byte[] data = rs.getBytes(2);
        // TODO: set number values to each type of the versions
        return new VersionInfo(new Version(data), VersionType.values()[type]);
    }
}
