package org.xblackcat.sunaj.service.storage.cached;

import org.xblackcat.sunaj.service.storage.*;


/**
 * Date: 15.04.2007
 *
 * @author ASUS
 */
public class CachedStorage implements IStorage {
    private final IStorage storage;

    /*
     * Caches
     */

    private final CachedForumDAO forumDAO;
    private final CachedForumGroupDAO forumGroupDAO;
    private final CachedMessageDAO messageDAO;
    private final CachedModerateDAO moderateDAO;
    private final CachedRatingDAO ratingDAO;
    private final CachedUserDAO userDAO;

    public CachedStorage(IStorage storage) {
        this.storage = storage;
        forumDAO = new CachedForumDAO(storage.getForumDAO());
        forumGroupDAO = new CachedForumGroupDAO(storage.getForumGroupDAO());
        messageDAO = new CachedMessageDAO(storage.getMessageDAO());
        moderateDAO = new CachedModerateDAO(storage.getModerateDAO());
        ratingDAO = new CachedRatingDAO(storage.getRatingDAO());
        userDAO = new CachedUserDAO(storage.getUserDAO());
    }

    public boolean checkStructure() {
        return storage.checkStructure();
    }

    public void initialize() throws StorageException {
        storage.initialize();
        forumDAO.purge();
        forumGroupDAO.purge();
        messageDAO.purge();
        moderateDAO.purge();
        ratingDAO.purge();
        userDAO.purge();
    }

    public IForumDAO getForumDAO() {
        return forumDAO;
    }

    public IForumGroupDAO getForumGroupDAO() {
        return forumGroupDAO;
    }

    public IMessageDAO getMessageDAO() {
        return messageDAO;
    }

    public IModerateDAO getModerateDAO() {
        return moderateDAO;
    }

    public INewMessageDAO getNewMessageDAO() {
        return storage.getNewMessageDAO();
    }

    public INewRatingDAO getNewRatingDAO() {
        return storage.getNewRatingDAO();
    }

    public IRatingDAO getRatingDAO() {
        return ratingDAO;
    }

    public IUserDAO getUserDAO() {
        return userDAO;
    }

    public IVersionDAO getVersionDAO() {
        return storage.getVersionDAO();
    }

    public IMiscDAO getMiscDAO() {
        return storage.getMiscDAO();
    }

}
