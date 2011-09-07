package org.xblackcat.rojac.service.storage.database.connection;

/**
 * @author xBlackCat
 */
public interface ISettings {
    String getUrl();

    String getShutdownUrl();

    String getUserName();

    String getPassword();
}
