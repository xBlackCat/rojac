package org.xblackcat.rojac.gui.dialog.db;

import org.xblackcat.sjpu.storage.IAH;
import org.xblackcat.sjpu.storage.StorageException;
import org.xblackcat.sjpu.storage.ann.Sql;

/**
 * 20.01.2015 14:10
 *
 * @author xBlackCat
 */
public interface ITestConnectionAH extends IAH {
    @Sql("SELECT 21*2")
    Long getAnswerToTheUltimateQuestionOfLifeTheUniverseAndEverything() throws StorageException;
}
