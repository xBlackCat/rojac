package org.xblackcat.rojac.service.datahandler;

import org.xblackcat.rojac.util.RojacUtils;

/**
 * 09.09.11 16:08
 *
 * @author xBlackCat
 */
public abstract class APacket implements IPacket {
    private static IDataDispatcher dispatcher;

    public static void setDispatcher(IDataDispatcher dispatcher) {
        assert RojacUtils.isMainThread();

        synchronized (APacket.class) {
            APacket.dispatcher = dispatcher;
        }
    }

    @Override
    public void dispatch() {
        assert RojacUtils.checkThread(true);
        IDataDispatcher dispatcher = getDispatcher();

        assert dispatcher != null;

        dispatcher.processPacket(this);
    }

    public static IDataDispatcher getDispatcher() {
        synchronized (APacket.class) {
            return dispatcher;
        }
    }
}
