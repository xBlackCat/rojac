package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.util.RojacUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 09.04.12 15:04
 *
 * @author xBlackCat
 */
class StreamingQueryHelper extends AQueryHelper {
    private final IResultFactory resultFactory;

    public StreamingQueryHelper(IConnectionFactory connectionFactory, IResultFactory resultFactory) {
        super(connectionFactory);
        this.resultFactory = resultFactory;
    }

    @Override
    public <T> IResult<T> execute(final IToObjectConverter<T> c, final String sql, final Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, getClass());

        final String debugAnchor = Property.ROJAC_SQL_DEBUG.get() ? RojacUtils.generateHash() : null;
        if (debugAnchor != null) {
            if (log.isTraceEnabled()) {
                log.trace("[" + debugAnchor + "] Execute query " + RojacUtils.constructDebugSQL(sql, parameters));
            }
        }

        try {
            Connection connection = connectionFactory.getConnection();
            try {
                return resultFactory.create(debugAnchor, c, connection, sql, parameters);
            } catch (SQLException e) {
                log.error("Can not create StreamingResult");
                connection.close();
                throw e;
            }
        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql, parameters), e);
        }
    }
}
