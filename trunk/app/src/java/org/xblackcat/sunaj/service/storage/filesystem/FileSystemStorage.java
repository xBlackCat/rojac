package org.xblackcat.sunaj.service.storage.filesystem;

import org.xblackcat.sunaj.service.storage.*;

import java.io.File;

/**
 * Date: 16.04.2007
 *
 * @author ASUS
 */

class FileSystemStorage implements IStorage {
    private final File root;

    FileSystemStorage(File root) {
        this.root = root;
    }

    /* Initialization routines */
    public boolean checkStructure() throws StorageException {
        return false;
    }

    public void initialize() throws StorageException {
    }

    public IForumDAO getForumDAO() {
        return null;
    }

    public IForumGroupDAO getForumGroupDAO() {
        return null;
    }

    public IMessageDAO getMessageDAO() {
        return null;
    }

    public IModerateDAO getModerateDAO() {
        return null;
    }

    public INewMessageDAO getNewMessageDAO() {
        return null;
    }

    public INewRatingDAO getNewRatingDAO() {
        return null;
    }

    public IRatingDAO getRatingDAO() {
        return null;
    }

    public IUserDAO getUserDAO() {
        return null;
    }
}
