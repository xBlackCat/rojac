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

    private final CachedForumAH forumDAO;
    private final CachedForumGroupAH forumGroupDAO;
    private final CachedMessageAH messageDAO;
    private final CachedModerateAH moderateDAO;
    private final CachedRatingAH ratingDAO;
    private final CachedUserAH userDAO;

    public CachedStorage(IStorage storage) {
        this.storage = storage;
        forumDAO = new CachedForumAH(storage.getForumAH());
        forumGroupDAO = new CachedForumGroupAH(storage.getForumGroupAH());
        messageDAO = new CachedMessageAH(storage.getMessageAH());
        moderateDAO = new CachedModerateAH(storage.getModerateAH());
        ratingDAO = new CachedRatingAH(storage.getRatingAH());
        userDAO = new CachedUserAH(storage.getUserAH());
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

    public IForumAH getForumAH() {
        return forumDAO;
    }

    public IForumGroupAH getForumGroupAH() {
        return forumGroupDAO;
    }

    public IMessageAH getMessageAH() {
        return messageDAO;
    }

    public IModerateAH getModerateAH() {
        return moderateDAO;
    }

    public INewMessageAH getNewMessageAH() {
        return storage.getNewMessageAH();
    }

    public INewRatingAH getNewRatingAH() {
        return storage.getNewRatingAH();
    }

    public IRatingAH getRatingAH() {
        return ratingDAO;
    }

    public IUserAH getUserAH() {
        return userDAO;
    }

    public IVersionAH getVersionAH() {
        return storage.getVersionAH();
    }

    public IMiscAH getMiscAH() {
        return storage.getMiscAH();
    }

}
