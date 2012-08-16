package org.xblackcat.rojac.service.storage.database.helper;

import org.xblackcat.rojac.service.IProgressTracker;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.util.RojacUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

/**
 * 09.04.12 11:22
 *
 * @author xBlackCat
 */
class ManagingQueryHelper extends AQueryHelper implements IManagingQueryHelper {
    protected final IConnectionFactory connectionFactory;

    public ManagingQueryHelper(IConnectionFactory connectionFactory, IDataFetcher dataFetcher) {
        super(dataFetcher);
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
    public void updateBatch(String sql, IProgressTracker tracker, Collection<Object[]> params) throws StorageException {
        assert RojacUtils.checkThread(false, getClass());

        try (Connection con = getConnection()) {
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

        } catch (SQLException e) {
            throw new StorageException("Can not execute query " + RojacUtils.constructDebugSQL(sql), e);
        }
    }

    @Override
    protected Connection getConnection() throws StorageException {
        try {
            return connectionFactory.getConnection();
        } catch (SQLException e) {
            throw new StorageException("Can't establish connection", e);
        }
    }

    @Override
    public IBatchedQueryHelper startBatch() throws StorageException {
        return new BatchedQueryHelper(getConnection(), dataFetcher);
    }

}
