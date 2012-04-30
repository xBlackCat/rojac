package org.xblackcat.rojac.service.storage.database.helper;

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

        final String debugAnchor = Property.ROJAC_DEBUG_SQL.get() ? RojacUtils.generateHash() : null;
        if (debugAnchor != null) {
            if (log.isTraceEnabled()) {
                log.trace("[" + debugAnchor + "] Execute query " + RojacUtils.constructDebugSQL(sql, parameters));
            }
        }

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (PreparedStatement st = con.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)) {
                    DBUtils.fillStatement(st, parameters);
                    long start = System.currentTimeMillis();

                    try (ResultSet rs = st.executeQuery()) {
                        if (debugAnchor != null) {
                            long executionTime = System.currentTimeMillis() - start;

                            Integer timeLine = Property.ROJAC_DEBUG_SQL_RUN_TIME_TRACK.get();
                            if (timeLine == null ||
                                    timeLine * 1000 <= executionTime) {
                                if (log.isTraceEnabled()) {
                                    log.trace("[" + debugAnchor + "] Query was executed in " + (System.currentTimeMillis() - start) + " ms");
                                }
                            }
                            start = System.currentTimeMillis();
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
}
