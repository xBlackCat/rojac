package org.xblackcat.rojac.service.storage.database.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private final Log log = LogFactory.getLog(getClass());

    protected final Connection connection;
    protected final IToObjectConverter<T> converter;
    protected final PreparedStatement statement;

    StreamingResult(String debugAnchor, IToObjectConverter<T> converter, Connection connection, String sql, Object... parameters) throws SQLException {
        this.converter = converter;

        this.connection = connection;
        long start = System.currentTimeMillis();
        statement = prepareStatement(sql);
        DBUtils.fillStatement(statement, parameters);

        if (debugAnchor != null) {
            if (log.isTraceEnabled()) {
                log.trace("[" + debugAnchor + "] Query was built in " + (System.currentTimeMillis() - start) + " ms");
            }
        }
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
            rs = statement.executeQuery();
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
