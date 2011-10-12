package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.storage.StorageDataException;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.util.RojacUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author ASUS
 */

public final class QueryHelper implements IQueryHelper {
    private final IConnectionFactory connectionFactory;

    public QueryHelper(IConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public <T> List<T> execute(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, QueryHelper.class);

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (PreparedStatement st = constructSql(con, sql, parameters)) {
                    try (ResultSet rs = st.executeQuery()) {
                        List<T> res = new ArrayList<>();
                        while (rs.next()) {
                            res.add(c.convert(rs));
                        }

                        return Collections.unmodifiableList(res);
                    }
                }
            }

        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql, parameters), e);
        }
    }

    @SafeVarargs
    @Override
    public final <K, O> Map<K, O> executeSingleBatch(IToObjectConverter<O> c, String sql, K... keys) throws StorageException {
        assert RojacUtils.checkThread(false, QueryHelper.class);

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (PreparedStatement st = con.prepareStatement(sql)) {
                    Map<K, O> resultMap = new HashMap<>();
                    for (K key : keys) {
                        fillStatement(st, key);

                        try (ResultSet rs = st.executeQuery()) {
                            if (rs.next()) {
                                resultMap.put(key, c.convert(rs));
                            }
                        }
                    }

                    return Collections.unmodifiableMap(resultMap);
                }

            }

        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql), e);
        }
    }

    @Override
    public final String getEngine() {
        return connectionFactory.getEngine();
    }

    @Override
    public void updateBatch(String sql, Object[]... params) throws StorageException {
        assert RojacUtils.checkThread(false, QueryHelper.class);

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (PreparedStatement preparedStatement = con.prepareStatement(sql)) {
                    for (Object[] parameters : params) {
                        fillStatement(preparedStatement, parameters);

                        preparedStatement.executeUpdate();
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
        if (col.size() > 1) {
            throw new StorageDataException("Expected one or zero results on query " + RojacUtils.constructDebugSQL(sql, parameters));
        }
        if (col.isEmpty()) {
            return null;
        } else {
            return col.iterator().next();
        }
    }

    @Override
    public int update(String sql, Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, QueryHelper.class);

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (PreparedStatement st = constructSql(con, sql, parameters)) {
                    return st.executeUpdate();
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
