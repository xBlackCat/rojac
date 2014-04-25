package org.xblackcat.rojac.service.storage.schema;

import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.Sql;

/**
 * 25.04.2014 10:39
 *
 * @author xBlackCat
 */
public interface ICheck extends IAH {
    @Sql("SELECT count(*) FROM version")
    int checkIntegrity() throws StorageException;
}
