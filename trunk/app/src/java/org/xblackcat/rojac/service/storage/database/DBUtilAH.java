package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.IUtilAH;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * @author ASUS
 */

final class DBUtilAH extends AnAH implements IUtilAH {
    DBUtilAH(IQueryHolder helper) {
        super(helper);
    }

    @Override
    public void updateForumsStat() throws StorageException {
        helper.update(DataQuery.CACHE_UPDATE_FORUMS_STAT);
    }

    @Override
    public void updateThreadsStat() throws StorageException {
        helper.update(DataQuery.CACHE_UPDATE_THREADS_STAT);
    }

    @Override
    public void updateLastPostId() throws StorageException {
        helper.update(DataQuery.CACHE_UPDATE_LASTPOST_ID);
    }

    @Override
    public void updateLastPostDate() throws StorageException {
        helper.update(DataQuery.CACHE_UPDATE_LASTPOST_DATE);
    }
}
