package org.xblackcat.rojac.service.storage.database;

import org.xblackcat.rojac.service.storage.importing.Cell;

/**
 * 11.10.11 11:21
 *
 * @author xBlackCat
 */
public interface IRowHandler {
    boolean handleRow(Cell[] row);
}
