package org.xblackcat.rojac.service.storage;

/**
 * 05.10.11 12:03
 *
 * @author xBlackCat
 */
public class MigrationQueries {
    private final String tablesQuery;
    private final String getTableDataQuery;
    private final String storeTableDataQuery;
    private final String nameQuote;

    public MigrationQueries(String tablesQuery, String getTableDataQuery, String storeTableDataQuery, String nameQuote) {
        this.tablesQuery = tablesQuery;
        this.getTableDataQuery = getTableDataQuery;
        this.storeTableDataQuery = storeTableDataQuery;
        this.nameQuote = nameQuote;
    }

    public String getTablesQuery() {
        return tablesQuery;
    }

    public String getTableDataQuery(String table) {
        return String.format(getTableDataQuery, table);
    }

    public String getStoreTableDataQuery() {
        return storeTableDataQuery;
    }

    public String quoteName(String name) {
        return String.format(nameQuote, name);
    }
}
