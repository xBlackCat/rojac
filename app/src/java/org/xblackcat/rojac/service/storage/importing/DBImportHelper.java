package org.xblackcat.rojac.service.storage.importing;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.service.storage.MigrationQueries;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.IRowHandler;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.util.DatabaseUtils;
import org.xblackcat.rojac.util.RojacUtils;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 05.10.11 10:13
 *
 * @author xBlackCat
 */
public class DBImportHelper implements IImportHelper {
    private static final Log log = LogFactory.getLog(DBImportHelper.class);

    private final IConnectionFactory connectionFactory;

    private final MigrationQueries queries;

    public DBImportHelper(IConnectionFactory connectionFactory) throws StorageException {
        this.connectionFactory = connectionFactory;
        String engine = connectionFactory.getEngine();

        try {
            queries = DatabaseUtils.loadImportingQueries(engine);
        } catch (IOException e) {
            throw new StorageException("Can not load migration queries", e);
        }
    }


    @Override
    public Collection<String> getItemsList() throws StorageException {
        assert RojacUtils.checkThread(false);

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (Statement st = con.createStatement()) {
                    try (ResultSet rs = st.executeQuery(queries.getTablesQuery())) {
                        List<String> res = new ArrayList<>();
                        while (rs.next()) {
                            res.add(rs.getString(1));
                        }

                        return Collections.unmodifiableList(res);
                    }
                }
            }

        } catch (SQLException e) {
            throw new StorageException("Can not read list of tables", e);
        }
    }

    @Override
    public void getData(IRowHandler rowHandler, String item) throws StorageException {
        assert RojacUtils.checkThread(false);

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (Statement st = con.createStatement()) {
                    try (ResultSet rs = st.executeQuery(queries.getTableDataQuery(item))) {
                        ResultSetMetaData metaData = rs.getMetaData();
                        int columnCount = metaData.getColumnCount();

                        while (rs.next()) {
                            Cell[] row = new Cell[columnCount];

                            for (int i = 0; i < columnCount; i++) {
                                row[i] = new Cell(
                                        metaData.getColumnName(1 + i),
                                        rs.getObject(1 + i)
                                );
                            }

                            if (!rowHandler.handleRow(row)) {
                                // Got error - aborting.
                                break;
                            }
                        }
                    }
                }
            }

        } catch (SQLException e) {
            throw new StorageException("Can not read row", e);
        }
    }

    @Override
    public IRowWriter getRowWriter(String item) throws StorageException {
        return new RowWriter(item);
    }

    @Override
    public int getRows(String item) throws StorageException {
        assert RojacUtils.checkThread(false);

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (Statement st = con.createStatement()) {
                    try (ResultSet rs = st.executeQuery(queries.getTableSizeQuery(item))) {
                        return rs.next() ? rs.getInt(1) : -1;
                    }
                }
            }

        } catch (SQLException e) {
            throw new StorageException("Can not get rows of table", e);
        }
    }

    private class RowWriter implements IRowWriter {
        private final String item;
        private Connection con = null;
        private PreparedStatement st = null;

        public RowWriter(String item) {
            this.item = item;
        }

        @Override
        public int storeRow(Cell[] cells) throws StorageException {
            assert RojacUtils.checkThread(false);

            if (st == null) {
                initialize(cells);
            }

            try {
                // Fill parameters if any
                for (int i = 0, dataLength = cells.length; i < dataLength; i++) {
                    Cell cell = cells[i];
                    st.setObject(i + 1, cell.getData());
                }

                return st.executeUpdate();
            } catch (SQLException e) {
                try {
                    con.close();
                } catch (SQLException e1) {
                    log.error("Can not close connection", e1);
                }
                throw new StorageException("Can not insert a row", e);
            }
        }

        @Override
        public void stop() throws StorageException {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                throw new StorageException("Can not close connection", e);
            }
        }

        private void initialize(Cell[] cells) throws StorageException {
            StringBuilder signs = new StringBuilder();
            StringBuilder names = new StringBuilder();

            for (Cell cell : cells) {
                signs.append(",?");
                names.append(',');
                names.append(queries.quoteName(cell.getName()));
            }

            String query = String.format(queries.getStoreTableDataQuery(), item, names.substring(1), signs.substring(1));

            try {
                con = connectionFactory.getConnection();
                try {
                    st = con.prepareStatement(query);
                } catch (SQLException e) {
                    if (log.isWarnEnabled()) {
                        log.warn("");
                    }

                    con.close();
                    throw e;
                }

            } catch (SQLException e) {
                throw new StorageException("Can not store row into " + item, e);
            }
        }
    }
}

