package org.xblackcat.rojac.service.storage.database;

/**
 * @author xBlackCat
 */
public final class SQL {
    private final String name;
    private final String sql;

    public SQL(String name, String sql) {
        this.name = name;
        this.sql = sql;
    }

    public String getName() {
        return name;
    }

    public String getSql() {
        return sql;
    }

    @Override
    public String toString() {
        return getName();
    }
}
