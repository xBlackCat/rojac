package org.xblackcat.rojac.service.storage.database.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * 09.04.12 15:06
 *
 * @author xBlackCat
 */
class StreamingResult<T> implements IResult<T> {
    private static final Log log = LogFactory.getLog(StreamingResult.class);

    protected final Connection connection;
    private final String debugAnchor;
    protected final IToObjectConverter<T> converter;
    protected final PreparedStatement statement;

    StreamingResult(String debugAnchor, IToObjectConverter<T> converter, Connection connection, String sql, Object... parameters) throws SQLException {
        this.debugAnchor = debugAnchor;
        this.converter = converter;

        this.connection = connection;
        statement = prepareStatement(sql);
        DBUtils.fillStatement(statement, parameters);
    }

    protected PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
    }

    @Override
    public void close() throws StorageException {
        try {
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new StorageException("Can not close connection", e);
        }
    }

    @Override
    public Iterator<T> iterator() {
        try {
            return new StreamingResultIterator<>(converter);
        } catch (SQLException e) {
            throw new IllegalStateException("Can not prepare iterator", e);
        }
    }

    private class StreamingResultIterator<T> implements Iterator<T> {
        private final ResultSet rs;
        private IToObjectConverter<T> converter;

        private StreamingResultIterator(IToObjectConverter<T> converter) throws SQLException {
            long start = System.currentTimeMillis();
            rs = statement.executeQuery();
            if (debugAnchor != null) {
                long executionTime = System.currentTimeMillis() - start;

                Integer timeLine = Property.ROJAC_DEBUG_SQL_RUN_TIME_TRACK.get();
                if (timeLine == null ||
                        timeLine * 1000 <= executionTime) {
                    if (log.isTraceEnabled()) {
                        log.trace("[" + debugAnchor + "] Query was executed in " + (System.currentTimeMillis() - start) + " ms");
                    }
                }
            }
            this.converter = converter;
        }

        @Override
        public boolean hasNext() {
            try {
                if (connection.isClosed() || statement.isClosed() || rs.isClosed()) {
                    return false;
                }

                if (rs.next()) {
                    return true;
                }

                // End of list reached
                rs.close();
                return false;
            } catch (SQLException e) {
                throw new IllegalStateException("Can not check next record", e);
            }
        }

        @Override
        public T next() {
            try {
                if (connection.isClosed() || statement.isClosed() || rs.isClosed()) {
                    throw new IllegalStateException("ResultSet is closed");
                }

                return converter.convert(rs);
            } catch (SQLException e) {
                throw new IllegalStateException("Can not read record", e);
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Can't remove from read-only iterator");
        }
    }
}
