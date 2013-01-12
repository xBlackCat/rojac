package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.AH;

/**
 * @author xBlackCat
 */
abstract class AnAH implements AH {
    protected final IQueryHolder helper;

    public AnAH(IQueryHolder helper) {
        this.helper = helper;
    }
}
