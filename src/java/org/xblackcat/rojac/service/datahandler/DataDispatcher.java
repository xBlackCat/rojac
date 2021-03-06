package org.xblackcat.rojac.service.datahandler;

import org.apache.commons.lang3.ArrayUtils;
import org.xblackcat.rojac.util.RojacUtils;

/**
 * @author xBlackCat
 */

public class DataDispatcher implements IDataDispatcher {
    private IDataHandler[] handlers = new IDataHandler[0];

    @Override
    public void addDataHandler(IDataHandler handler) {
        assert RojacUtils.checkThread(true);

        handlers = ArrayUtils.add(this.handlers, handler);
    }

    @Override
    public void removeDataHandler(IDataHandler handler) {
        assert RojacUtils.checkThread(true);

        handlers = ArrayUtils.removeElement(this.handlers, handler);
    }

    @Override
    public void processPacket(IPacket packet) {
        assert RojacUtils.checkThread(true);

        for (IDataHandler h : handlers) {
            h.processPacket(packet);
        }
    }
}
