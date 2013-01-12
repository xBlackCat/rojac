package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 14.08.12 16:31
 *
 * @author xBlackCat
 */
public interface IDataFetcher {
    public <T> IResult<T> execute(
            Connection connection, final IToObjectConverter<T> c,
            final String sql,
            final Object... parameters
    ) throws SQLException;
}
