package org.xblackcat.rojac.service.storage.database.connection;

/**
 * 09.09.11 12:15
 *
 * @author xBlackCat
 */
public class DatabaseSettings {
    protected final String engine;
    protected final String url;
    protected final String shutdownUrl;
    protected final String userName;
    protected final String password;
    protected final Class<?> jdbcDriverClass;

    public DatabaseSettings(String engine, String url, String shutdownUrl, String userName, String password, Class<?> jdbcDriverClass) {
        this.engine = engine;
        this.url = url;
        this.shutdownUrl = shutdownUrl;
        this.userName = userName;
        this.password = password;
        this.jdbcDriverClass = jdbcDriverClass;
    }

    public String getEngine() {
        return engine;
    }

    public String getUrl() {
        return url;
    }

    public String getShutdownUrl() {
        return shutdownUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Class<?> getJdbcDriverClass() {
        return jdbcDriverClass;
    }
}
