package org.xblackcat.rojac.service.storage.database.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.util.RojacUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 14.08.12 16:31
 *
 * @author xBlackCat
 */
public class StreamingDataFetcher implements IDataFetcher {
    private static final Log log = LogFactory.getLog(StreamingDataFetcher.class);

    private final IResultFactory resultFactory;

    public StreamingDataFetcher(IResultFactory resultFactory) {
        this.resultFactory = resultFactory;
    }

    @Override
    public <T> IResult<T> execute(
            Connection connection,
            IToObjectConverter<T> c,
            String sql,
            Object... parameters
    ) throws SQLException {
        final String debugAnchor = Property.ROJAC_DEBUG_SQL.get() ? RojacUtils.generateHash() : null;
        if (debugAnchor != null) {
            if (log.isTraceEnabled()) {
                log.trace("[" + debugAnchor + "] Execute query " + RojacUtils.constructDebugSQL(sql, parameters));
            }
        }

        try {
            return resultFactory.create(debugAnchor, c, connection, sql, parameters);
        } catch (SQLException e) {
            log.error("Can not create StreamingResult");
            connection.close();
            throw e;
        }
    }
}
