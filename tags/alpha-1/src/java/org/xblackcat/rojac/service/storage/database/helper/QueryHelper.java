package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.storage.StorageDataException;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.DBStorage;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ASUS
 */

public final class QueryHelper implements IQueryHelper {
    private final IConnectionFactory connectionFactory;

    public QueryHelper(IConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public <T> Collection<T> execute(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException {
        RojacUtils.checkThread(false, DBStorage.class);
        try {
            Connection con = connectionFactory.getConnection();
            try {
                PreparedStatement st = constructSql(con, sql, parameters);
                try {
                    ResultSet rs = st.executeQuery();
                    try {
                        List<T> res = new ArrayList<T>();
                        while (rs.next()) {
                            res.add(c.convert(rs));
                        }

                        return Collections.unmodifiableCollection(res);
                    } finally {
                        rs.close();
                    }
                } finally {
                    st.close();
                }
            } finally {
                con.close();
            }
        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql, parameters), e);
        }
    }

    @Override
    public <K, O> Map<K, O> executeSingleBatch(IToObjectConverter<O> c, String sql, K... keys) throws StorageException {
        RojacUtils.checkThread(false, DBStorage.class);
        try {
            Connection con = connectionFactory.getConnection();
            try {
                PreparedStatement st = con.prepareStatement(sql);
                try {
                    Map<K, O> resultMap = new HashMap<K,O>();
                    for (K key : keys) {
                        fillStatement(st, key);
                        ResultSet rs = st.executeQuery();
                        try {
                            if (rs.next()) {
                                resultMap.put(key, c.convert(rs));
                            }
                        } finally {
                            rs.close();
                        }
                    }

                    return Collections.unmodifiableMap(resultMap);
                } finally {
                    st.close();
                }
            } finally {
                con.close();
            }
        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql), e);
        }
    }

    @Override
    public <T> T executeSingle(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException {
        RojacUtils.checkThread(false, DBStorage.class);
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
        RojacUtils.checkThread(false, DBStorage.class);
        try {
            Connection con = connectionFactory.getConnection();
            try {
                PreparedStatement st = constructSql(con, sql, parameters);
                try {
                    return st.executeUpdate();
                } finally {
                    st.close();
                }
            } finally {
                con.close();
            }
        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql, parameters), e);
        }
    }

    private static PreparedStatement constructSql(Connection con, String sql, Object... parameters) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql);
        fillStatement(pstmt, parameters);

        return pstmt;
    }

    private static void fillStatement(PreparedStatement pstmt, Object... parameters) throws SQLException {
        // Fill parameters if any
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i + 1, parameters[i]);
            }
        }
    }
}
