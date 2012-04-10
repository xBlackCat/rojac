package org.xblackcat.rojac.service.storage;

/**
 * 10.04.12 15:51
 *
 * @author xBlackCat
 */
public class StorageSettings {
    protected final String engineName;
    protected final String engine;

    public StorageSettings(String engine, String engineName) {
        this.engine = engine;
        this.engineName = engineName;
    }

    public String getEngineName() {
        return engineName;
    }

    public String getEngine() {
        return engine;
    }
}
