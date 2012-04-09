package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.IProgressTracker;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.util.RojacUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author ASUS
 */

final class QueryHelper extends AQueryHelper {

    public QueryHelper(IConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    public <T> IResult<T> execute(final IToObjectConverter<T> c, final String sql, final Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, QueryHelper.class);

        final String debugAnchor = Property.ROJAC_SQL_DEBUG.get() ? RojacUtils.generateHash() : null;
        if (debugAnchor != null) {
            if (log.isTraceEnabled()) {
                log.trace("[" + debugAnchor + "] Execute query " + RojacUtils.constructDebugSQL(sql, parameters));
            }
        }

        try {
            try (Connection con = connectionFactory.getConnection()) {
                long start = System.currentTimeMillis();
                try (PreparedStatement st = con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
                    DBUtils.fillStatement(st, parameters);
                    if (debugAnchor != null) {
                        if (log.isTraceEnabled()) {
                            log.trace("[" + debugAnchor + "] Query was built in " + (System.currentTimeMillis() - start) + " ms");
                            start = System.currentTimeMillis();
                        }
                    }

                    try (ResultSet rs = st.executeQuery()) {
                        if (debugAnchor != null) {
                            if (log.isTraceEnabled()) {
                                log.trace("[" + debugAnchor + "] Query was executed in " + (System.currentTimeMillis() - start) + " ms");
                                start = System.currentTimeMillis();
                            }
                        }


                        final List<T> res = new ArrayList<>();
                        while (rs.next()) {
                            res.add(c.convert(rs));
                        }

                        if (debugAnchor != null) {
                            if (log.isTraceEnabled()) {
                                log.trace("[" + debugAnchor + "] " + res.size() + " row(s) fetched in " + (System.currentTimeMillis() - start) + " ms");
                            }
                        }
                        return new PreloadedResult<>(res);
                    }
                }
            }

        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql, parameters), e);
        }
    }

    @Override
    public void updateBatch(String sql, IProgressTracker tracker, Collection<Object[]> params) throws StorageException {
        assert RojacUtils.checkThread(false, QueryHelper.class);

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
        assert RojacUtils.checkThread(false, QueryHelper.class);

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
