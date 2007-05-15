package org.xblackcat.sunaj.service.storage;

/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */

public interface IStorage {
    /* Initialization routines */
    boolean checkStructure();

    void initialize() throws StorageException;

    IForumDAO getForumDAO();

    IForumGroupDAO getForumGroupDAO();

    IMessageDAO getMessageDAO();

    IModerateDAO getModerateDAO();

    INewMessageDAO getNewMessageDAO();

    INewRatingDAO getNewRatingDAO();

    IRatingDAO getRatingDAO();

    IUserDAO getUserDAO();

    IVersionDAO getVersionDAO();

    IMiscDAO getMiscDAO();
}
