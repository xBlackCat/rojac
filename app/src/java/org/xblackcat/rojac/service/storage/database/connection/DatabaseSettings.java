package org.xblackcat.rojac.service.storage.database.connection;

/**
 * 09.09.11 12:15
 *
 * @author xBlackCat
 */
public class DatabaseSettings {
    protected final String engineName;
    protected final String engine;
    protected final String url;
    protected final String shutdownUrl;
    protected final String userName;
    protected final String password;
    protected final Class<?> jdbcDriverClass;

    public DatabaseSettings(String engineName, String engine, String url, String shutdownUrl, String userName, String password, Class<?> jdbcDriverClass) {
        this.engineName = engineName;
        this.engine = engine;
        this.url = url;
        this.shutdownUrl = shutdownUrl;
        this.userName = userName;
        this.password = password;
        this.jdbcDriverClass = jdbcDriverClass;
    }

    public String getEngineName() {
        return engineName;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DatabaseSettings that = (DatabaseSettings) o;

        if (!engine.equals(that.engine)) {
            return false;
        }
        if (!jdbcDriverClass.equals(that.jdbcDriverClass)) {
            return false;
        }
        if (password != null ? !password.equals(that.password) : that.password != null) {
            return false;
        }
        if (shutdownUrl != null ? !shutdownUrl.equals(that.shutdownUrl) : that.shutdownUrl != null) {
            return false;
        }
        if (!url.equals(that.url)) {
            return false;
        }

        return userName != null ? userName.equals(that.userName) : that.userName == null;

    }

    @Override
    public int hashCode() {
        int result = engine.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + (shutdownUrl != null ? shutdownUrl.hashCode() : 0);
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + jdbcDriverClass.hashCode();
        return result;
    }
}
