package org.xblackcat.sunaj.service.storage.database.helper;

import org.xblackcat.sunaj.service.storage.StorageDataException;
import org.xblackcat.sunaj.service.storage.StorageException;
import org.xblackcat.sunaj.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.sunaj.service.storage.database.convert.IToObjectConvertor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Date: 17.04.2007
 *
 * @author ASUS
 */

public final class QueryHelper implements IQueryHelper {
    private final IConnectionFactory connectionFactory;

    public QueryHelper(IConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public <T> Collection<T> execute(IToObjectConvertor<T> c, String sql, Object... parameters) throws StorageException {
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
            throw new StorageException("Can not execute query " + sql, e);
        }
    }

    public <T> T executeSingle(IToObjectConvertor<T> c, String sql, Object... parameters) throws StorageException {
        Collection<T> col = execute(c, sql, parameters);
        if (col.size() > 1) {
            throw new StorageDataException("Expected one or zero results on query " + sql);
        }
        if (col.isEmpty()) {
            return null;
        } else {
            return col.iterator().next();
        }
    }

    public void update(String sql, Object... parameters) throws StorageException {
        try {
            Connection con = connectionFactory.getConnection();
            try {
                PreparedStatement st = constructSql(con, sql, parameters);
                try {
                    st.executeUpdate();
                } finally {
                    st.close();
                }
            } finally {
                con.close();
            }
        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + sql, e);
        }
    }

    static PreparedStatement constructSql(Connection con, String sql, Object... parameters) throws SQLException {
        PreparedStatement pstmt = con.prepareStatement(sql);
        // Fill parameters if any
        if (parameters != null) {
            for (int i = 0; i < parameters.length; i++) {
                pstmt.setObject(i + 1, parameters[i]);
            }
        }
        return pstmt;
    }
}
