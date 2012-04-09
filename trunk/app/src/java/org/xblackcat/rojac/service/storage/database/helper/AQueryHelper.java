package org.xblackcat.rojac.service.storage.database.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.IProgressTracker;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.util.RojacUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

/**
 * 09.04.12 11:22
 *
 * @author xBlackCat
 */
abstract class AQueryHelper implements IQueryHelper {
    protected final Log log = LogFactory.getLog(getClass());
    protected final IConnectionFactory connectionFactory;

    public AQueryHelper(IConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public final String getEngine() {
        return connectionFactory.getEngine();
    }

    @Override
    public void shutdown() {
        connectionFactory.shutdown();
    }

    @Override
    public <T> T executeSingle(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, QueryHelper.class);

        try (IResult<T> result = execute(c, sql, parameters)) {
            Iterator<T> col = result.iterator();

            if (!col.hasNext()) {
                return null;
            }

            T object = col.next();
            assert !col.hasNext() : "Expected one or zero results on query " + RojacUtils.constructDebugSQL(sql, parameters);
            return object;
        }
    }

    @Override
    public void updateBatch(String sql, IProgressTracker tracker, Collection<Object[]> params) throws StorageException {
        assert RojacUtils.checkThread(false, getClass());

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                    int i = 0;
                    int paramsLength = params.size();
                    for (Object[] param : params) {
                        if (tracker != null) {
                            tracker.updateProgress(i, paramsLength);
                        }
                        DBUtils.fillStatement(preparedStatement, param);

                        preparedStatement.executeUpdate();
                        i++;
                    }
                }

            }

        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql), e);
        }
    }

    @Override
    public int update(String sql, Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, getClass());

        String debugAnchor = Property.ROJAC_SQL_DEBUG.get() ? RojacUtils.generateHash() : null;
        if (debugAnchor != null) {
            if (log.isTraceEnabled()) {
                log.trace("[" + debugAnchor + "] Execute update " + RojacUtils.constructDebugSQL(sql, parameters));
            }
        }


        try {
            try (Connection con = connectionFactory.getConnection()) {
                long start = System.currentTimeMillis();
                try (PreparedStatement st = con.prepareStatement(sql)) {
                    DBUtils.fillStatement(st, parameters);
                    if (debugAnchor != null) {
                        if (log.isTraceEnabled()) {
                            log.trace("[" + debugAnchor + "] Update was prepared in " + (System.currentTimeMillis() - start) + " ms");
                            start = System.currentTimeMillis();
                        }
                    }
                    int rows = st.executeUpdate();
                    if (debugAnchor != null) {
                        if (log.isTraceEnabled()) {
                            log.trace("[" + debugAnchor + "] Update was executed in " + (System.currentTimeMillis() - start) + " ms");
                        }
                    }
                    return rows;
                }

            }

        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql, parameters), e);
        }
    }
}
