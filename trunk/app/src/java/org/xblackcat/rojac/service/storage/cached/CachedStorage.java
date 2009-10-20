package org.xblackcat.rojac.service.storage.cached;

import org.xblackcat.rojac.service.storage.*;


/**
 * @author ASUS
 */
public class CachedStorage implements IStorage {
    private final IStorage storage;

    /*
     * Caches
     */

    private final CachedForumAH forumAH;
    private final CachedForumGroupAH forumGroupAH;
    private final CachedMessageAH messageAH;
    private final CachedModerateAH moderateAH;
    private final CachedRatingAH ratingAH;
    private final CachedUserAH userAH;

    public CachedStorage(IStorage storage) {
        this.storage = storage;
        forumAH = new CachedForumAH(storage.getForumAH());
        forumGroupAH = new CachedForumGroupAH(storage.getForumGroupAH());
        messageAH = new CachedMessageAH(storage.getMessageAH());
        moderateAH = new CachedModerateAH(storage.getModerateAH());
        ratingAH = new CachedRatingAH(storage.getRatingAH());
        userAH = new CachedUserAH(storage.getUserAH());
    }

    public void initialize() throws StorageException {
        storage.initialize();
        forumAH.purge();
        forumGroupAH.purge();
        messageAH.purge();
        moderateAH.purge();
        ratingAH.purge();
        userAH.purge();
    }

    public IForumAH getForumAH() {
        return forumAH;
    }

    public IForumGroupAH getForumGroupAH() {
        return forumGroupAH;
    }

    public IMessageAH getMessageAH() {
        return messageAH;
    }

    public IModerateAH getModerateAH() {
        return moderateAH;
    }

    public INewMessageAH getNewMessageAH() {
        return storage.getNewMessageAH();
    }

    public INewModerateAH getNewModerateAH() {
        return storage.getNewModerateAH();
    }

    public INewRatingAH getNewRatingAH() {
        return storage.getNewRatingAH();
    }

    public IRatingAH getRatingAH() {
        return ratingAH;
    }

    public IUserAH getUserAH() {
        return userAH;
    }

    public IVersionAH getVersionAH() {
        return storage.getVersionAH();
    }

    public IMiscAH getMiscAH() {
        return storage.getMiscAH();
    }

}
