package org.xblackcat.rojac.service.storage.database.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.IProgressTracker;
import org.xblackcat.rojac.service.options.Property;
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
import java.util.Collections;
import java.util.List;

/**
 * @author ASUS
 */

public final class QueryHelper implements IQueryHelper {
    private static final Log log = LogFactory.getLog(QueryHelper.class);

    private final IConnectionFactory connectionFactory;

    public QueryHelper(IConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public <T> List<T> execute(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, QueryHelper.class);

        String debugAnchor = Property.ROJAC_SQL_DEBUG.get() ? RojacUtils.generateHash() : null;
        if (debugAnchor != null) {
            if (log.isTraceEnabled()) {
                log.trace("[" + debugAnchor + "] Execute query " + RojacUtils.constructDebugSQL(sql, parameters));
            }
        }

        try {
            try (Connection con = connectionFactory.getConnection()) {
                long start = System.currentTimeMillis();
                try (PreparedStatement st = constructSql(con, sql, parameters)) {
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


                        List<T> res = new ArrayList<>();
                        while (rs.next()) {
                            res.add(c.convert(rs));
                        }

                        if (debugAnchor != null) {
                            if (log.isTraceEnabled()) {
                                log.trace("[" + debugAnchor + "] " + res.size() + " row(s) fetched in " + (System.currentTimeMillis() - start) + " ms");
                            }
                        }
                        return Collections.unmodifiableList(res);
                    }
                }
            }

        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql, parameters), e);
        }
    }

    @Override
    public final String getEngine() {
        return connectionFactory.getEngine();
    }

    @Override
    public void updateBatch(String sql, IProgressTracker tracker, Object[]... params) throws StorageException {
        assert RojacUtils.checkThread(false, QueryHelper.class);

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                    int i = 0, paramsLength = params.length;
                    while (i < paramsLength) {
                        if (tracker != null) {
                            tracker.updateProgress(i, paramsLength);
                        }
                        fillStatement(preparedStatement, params[i]);

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
    public void shutdown() {
        connectionFactory.shutdown();
    }

    @Override
    public <T> T executeSingle(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, QueryHelper.class);

        Collection<T> col = execute(c, sql, parameters);
        assert col.size() < 2 : "Expected one or zero but got " + col.size() + " results on query " + RojacUtils.constructDebugSQL(sql, parameters);
        if (col.isEmpty()) {
            return null;
        } else {
            return col.iterator().next();
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
                try (PreparedStatement st = constructSql(con, sql, parameters)) {
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

    private static PreparedStatement constructSql(Connection con, String sql, Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = con.prepareStatement(sql);
        fillStatement(preparedStatement, parameters);

        return preparedStatement;
    }

    private static void fillStatement(PreparedStatement preparedStatement, Object... parameters) throws SQLException {
        // Fill parameters if any
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] instanceof Boolean) {
                    preparedStatement.setInt(i + 1, ((Boolean) (parameters[i])) ? 1 : 0);
                } else {
                    preparedStatement.setObject(i + 1, parameters[i]);
                }
            }
        }
    }
}
