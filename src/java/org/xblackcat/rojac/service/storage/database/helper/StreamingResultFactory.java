package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 09.04.12 15:12
 *
 * @author xBlackCat
 */
class StreamingResultFactory implements IResultFactory {
    @Override
    public <T> IResult<T> create(String debugAnchor, IToObjectConverter<T> converter, Connection connection, String sql, Object... parameters) throws SQLException {
        return new StreamingResult<>(debugAnchor, converter, connection, sql, parameters);
    }
}
