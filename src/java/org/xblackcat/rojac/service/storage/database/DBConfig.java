package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.StorageSettings;
import org.xblackcat.sjpu.storage.connection.IDBConfig;

/**
 * 22.04.2014 16:43
 *
 * @author xBlackCat
 */
public class DBConfig extends StorageSettings implements IDBConfig {
    private final String driver;
    private final String url;
    private final String user;
    private final String password;

    public DBConfig(String engine, String engineName, String driver, String url, String user, String password) {
        super(engine, engineName);
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getPoolSize() {
        return 10;
    }
}
