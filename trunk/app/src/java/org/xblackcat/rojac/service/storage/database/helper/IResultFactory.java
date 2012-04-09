package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 09.04.12 15:10
 *
 * @author xBlackCat
 */
interface IResultFactory {
    <T> IResult<T> create(String debugAnchor, IToObjectConverter<T> converter, Connection connection, String sql, Object... parameters) throws SQLException;
}

