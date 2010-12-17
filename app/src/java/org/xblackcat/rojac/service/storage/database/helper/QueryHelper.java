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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author ASUS
 */

public final class QueryHelper implements IQueryHelper {
    private final IConnectionFactory connectionFactory;
    private final Lock writeLock;
    private final Lock readLock;


    public QueryHelper(IConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        ReadWriteLock lock = new ReentrantReadWriteLock();
        writeLock = lock.writeLock();
        readLock = lock.readLock();
    }

    @Override
    public <T> List<T> execute(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, DBStorage.class);
        try {
            Connection con = connectionFactory.getReadConnection();
            try {
                PreparedStatement st = constructSql(con, sql, parameters);
                try {
                    readLock.lock();
                    try {
                        ResultSet rs = st.executeQuery();
                        try {
                            List<T> res = new ArrayList<T>();
                            while (rs.next()) {
                                res.add(c.convert(rs));
                            }

                            return Collections.unmodifiableList(res);
                        } finally {
                            rs.close();
                        }
                    } finally {
                        readLock.unlock();
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
        assert RojacUtils.checkThread(false, DBStorage.class);
        try {
            Connection con = connectionFactory.getReadConnection();
            try {
                PreparedStatement st = con.prepareStatement(sql);
                try {
                    Map<K, O> resultMap = new HashMap<K,O>();
                    for (K key : keys) {
                        fillStatement(st, key);

                        readLock.lock();
                        try {
                            ResultSet rs = st.executeQuery();
                            try {
                                if (rs.next()) {
                                    resultMap.put(key, c.convert(rs));
                                }
                            } finally {
                                rs.close();
                            }
                        } finally {
                            readLock.unlock();
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
    public void shutdown() {
        connectionFactory.shutdown();
    }

    @Override
    public <T> T executeSingle(IToObjectConverter<T> c, String sql, Object... parameters) throws StorageException {
        assert RojacUtils.checkThread(false, DBStorage.class);
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
        assert RojacUtils.checkThread(false, DBStorage.class);
        try {
            Connection con = connectionFactory.getWriteConnection();
            try {
                PreparedStatement st = constructSql(con, sql, parameters);
                try {
                    writeLock.lock();
                    try {
                        return st.executeUpdate();
                    } finally {
                        writeLock.unlock();
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

    private static PreparedStatement constructSql(Connection con, String sql, Object... parameters) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql);
        fillStatement(pstmt, parameters);

        return pstmt;
    }

    private static void fillStatement(PreparedStatement pstmt, Object... parameters) throws SQLException {
        // Fill parameters if any
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                if (parameters[i] instanceof Boolean) {
                    pstmt.setInt(i + 1, ((Boolean)(parameters[i])) ? 1 : 0);
                } else {
                    pstmt.setObject(i + 1, parameters[i]);
                }
            }
        }
    }
}
