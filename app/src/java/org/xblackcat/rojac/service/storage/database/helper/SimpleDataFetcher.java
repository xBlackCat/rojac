package org.xblackcat.rojac.service.storage.database.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.util.RojacUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 14.08.12 16:38
 *
 * @author xBlackCat
 */
public class SimpleDataFetcher implements IDataFetcher {
    private static final Log log = LogFactory.getLog(SimpleDataFetcher.class);

    @Override
    public <T> IResult<T> execute(
            Connection con,
            IToObjectConverter<T> c,
            String sql,
            Object... parameters
    ) throws SQLException {
        assert RojacUtils.checkThread(false, SimpleDataFetcher.class);

        final String debugAnchor = Property.ROJAC_DEBUG_SQL.get() ? RojacUtils.generateHash() : null;
        if (debugAnchor != null) {
            if (log.isTraceEnabled()) {
                log.trace("[" + debugAnchor + "] Execute query " + RojacUtils.constructDebugSQL(sql, parameters));
            }
        }

            try (PreparedStatement st = con.prepareStatement(
                    sql,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY
            )) {
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
}
