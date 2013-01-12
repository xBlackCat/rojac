package org.xblackcat.rojac.service.storage.database.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.util.RojacUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * 14.08.12 16:27
 *
 * @author xBlackCat
 */
public abstract class AQueryHelper implements IQueryHelper {
    protected final Log log = LogFactory.getLog(getClass());
    protected final IDataFetcher dataFetcher;

    public AQueryHelper(IDataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }

    @Override
    public <T> IResult<T> execute(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException {
        Connection connection = getConnection();
        try {
            return dataFetcher.execute(connection, c, sql, parameters);
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                log.error("Can't close connection", e1);
            }
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql, parameters), e);
        }
    }

    @Override
    public <T> T executeSingle(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, AQueryHelper.class);

        try {
            try (IResult<T> result = execute(c, sql, parameters)) {
                Iterator<T> col = result.iterator();

                if (!col.hasNext()) {
                    return null;
                }

                T object = col.next();
                assert !col.hasNext() : "Expected one or zero results on query " +
                        RojacUtils.constructDebugSQL(
                                sql,
                                parameters
                        );
                return object;
            }
        } catch (IllegalStateException e) {
            if (e.getCause() instanceof StorageException) {
                throw (StorageException) e.getCause();
            } else if (e.getCause() instanceof SQLException) {
                throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql, parameters));
            } else {
                // Another runtime exception
                throw e;
            }
        }
    }

    @Override
    public int update(String sql, Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, getClass());

        String debugAnchor = Property.ROJAC_DEBUG_SQL.get() ? RojacUtils.generateHash() : null;
        if (debugAnchor != null) {
            if (log.isTraceEnabled()) {
                log.trace("[" + debugAnchor + "] Execute update " + RojacUtils.constructDebugSQL(sql, parameters));
            }
        }


        try (Connection con = getConnection()) {
            try (PreparedStatement st = con.prepareStatement(sql)) {
                DBUtils.fillStatement(st, parameters);
                long start = System.currentTimeMillis();

                int rows = st.executeUpdate();

                if (debugAnchor != null) {
                    final long executionTime = System.currentTimeMillis() - start;

                    final Integer timeLine = Property.ROJAC_DEBUG_SQL_RUN_TIME_TRACK.get();
                    if (timeLine == null ||
                            timeLine * 1000 <= executionTime) {
                        if (log.isTraceEnabled()) {
                            log.trace("[" + debugAnchor + "] Update was executed in " + executionTime + " ms. " + rows + " row(s) affected.");
                        }
                    }
                }
                return rows;
            }

        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql, parameters), e);
        }
    }

    protected abstract Connection getConnection() throws StorageException;
}
