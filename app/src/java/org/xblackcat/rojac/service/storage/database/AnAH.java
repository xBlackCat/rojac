package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.AH;

/**
 * @author xBlackCat
 */
abstract class AnAH implements AH {
    protected final IQueryExecutor helper;

    public AnAH(IQueryExecutor helper) {
        this.helper = helper;
    }
}
