package org.xblackcat.rojac.service.datahandler;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;

/**
 * @author xBlackCat
 */

public class DataDispatcher implements IDataDispatcher {
    private IDataHandler[] handlers = new IDataHandler[0];

    @Override
    public void addDataHandler(IDataHandler handler) {
        RojacUtils.checkThread(true, DataDispatcher.class);

        handlers = (IDataHandler[]) ArrayUtils.add(this.handlers, handler);
    }

    @Override
    public void removeDataHandler(IDataHandler handler) {
        RojacUtils.checkThread(true, DataDispatcher.class);

        handlers = (IDataHandler[]) ArrayUtils.removeElement(this.handlers, handler);
    }

    @Override
    public void processPacket(ProcessPacket results) {
        if (Property.ROJAC_DEBUG_MODE.get()) {
            RojacUtils.checkThread(true, DataDispatcher.class);
        }

        for (IDataHandler h : handlers) {
            h.processPacket(results);
        }
    }
}
