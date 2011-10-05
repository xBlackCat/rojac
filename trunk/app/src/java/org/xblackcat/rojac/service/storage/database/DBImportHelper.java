package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.data.Cell;
import org.xblackcat.rojac.service.storage.IImportHelper;
import org.xblackcat.rojac.service.storage.MigrationQueries;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.service.storage.database.connection.IConnectionFactory;
import org.xblackcat.rojac.util.DatabaseUtils;
import org.xblackcat.rojac.util.RojacUtils;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * 05.10.11 10:13
 *
 * @author xBlackCat
 */
class DBImportHelper implements IImportHelper {
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
    public Iterable<Cell[]> getData(String item) throws StorageException {
        assert RojacUtils.checkThread(false);

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (Statement st = con.createStatement()) {
                    try (ResultSet rs = st.executeQuery(queries.getTableDataQuery(item))) {
                        return new LazyResultSet(rs);
                    }
                }
            }

        } catch (SQLException e) {
            throw new StorageException("Can not read list of tables", e);
        }
    }

    @Override
    public int storeItem(String item, Cell[] data) throws StorageException {
        assert RojacUtils.checkThread(false);

        StringBuilder signs = new StringBuilder();
        StringBuilder names = new StringBuilder();

        for (Cell cell : data) {
            signs.append(",?");
            names.append(',');
            names.append(queries.quoteName(cell.getName()));
        }

        String query = String.format(queries.getStoreTableDataQuery(), item, names.substring(1), signs.substring(1));

        try {
            try (Connection con = connectionFactory.getConnection()) {
                try (PreparedStatement st = con.prepareStatement(query)) {
                    // Fill parameters if any
                    for (int i = 0, dataLength = data.length; i < dataLength; i++) {
                        Cell cell = data[i];
                        st.setObject(i + 1, cell.getData());
                    }
                    return st.executeUpdate();
                }

            }

        } catch (SQLException e) {
            throw new StorageException("Can not store row into " + item, e);
        }
    }

    private static class LazyResultSet implements Iterable<Cell[]> {
        private final ResultSet rs;

        private LazyResultSet(ResultSet rs) {
            this.rs = rs;
        }

        @Override
        public Iterator<Cell[]> iterator() {
            return new Iterator<Cell[]>() {
                private Boolean hasNext;
                private Cell[] row;

                @Override
                public boolean hasNext() {
                    if (hasNext == null) {
                        try {
                            hasNext = rs.next();

                            if (hasNext) {
                                ResultSetMetaData metaData = rs.getMetaData();
                                int columnCount = metaData.getColumnCount();

                                row = new Cell[columnCount];

                                for (int i = 0; i < columnCount; i++) {
                                    row[i] = new Cell(
                                            metaData.getColumnName(1 + i),
                                            rs.getObject(1 + i)
                                    );
                                }

                            } else {
                                rs.close();
                                hasNext = false;
                            }
                        } catch (SQLException e) {
                            throw new IllegalStateException("Can not read next row", e);
                        }
                    }

                    return hasNext;
                }

                @Override
                public Cell[] next() {
                    if (hasNext == null) {
                        if (!hasNext()) {
                            throw new NoSuchElementException();
                        }
                    }
                    if (!Boolean.TRUE.equals(hasNext)) {
                        throw new NoSuchElementException();
                    }
                    hasNext = null;
                    return row;
                }

                @Override
                public void remove() {
                    try {
                        rs.deleteRow();
                    } catch (SQLException e) {
                        throw new IllegalStateException("Can not delete row");
                    }
                }
            };
        }
    }
}

