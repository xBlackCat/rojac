package org.xblackcat.rojac.service.storage.database.helper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.IResult;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.service.storage.database.convert.IToObjectConverter;
import org.xblackcat.rojac.util.RojacUtils;

import java.util.Iterator;

/**
 * 09.04.12 11:22
 *
 * @author xBlackCat
 */
public abstract class AQueryHelper implements IQueryHelper {
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
}
