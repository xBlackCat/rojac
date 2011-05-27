package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public interface IDataDispatcher extends IDataHandler {
    void addDataHandler(IDataHandler handler);

    void removeDataHandler(IDataHandler handler);
}
