package org.xblackcat.rojac.service.datahandler;

import org.xblackcat.rojac.util.RojacUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class to handle packets dispatching classes.
 *
 * @author xBlackCat
 */

public class PacketDispatcher {
    private final Map<Class<?>, IPacketProcessor<IPacket>> processors = new HashMap<>();

    @SafeVarargs
    public PacketDispatcher(IPacketProcessor<? extends IPacket>... processors) {
        if (processors == null) {
            return;
        }

        for (IPacketProcessor<? extends IPacket> processor : processors) {
            if (processor != null) {
                @SuppressWarnings("unchecked") final IPacketProcessor<IPacket> p = (IPacketProcessor<IPacket>) processor;
                this.processors.put(RojacUtils.getGenericClass(processor.getClass()), p);
            }
        }
    }

    public void dispatch(IPacket p) {
        for (Map.Entry<Class<?>, IPacketProcessor<IPacket>> e : processors.entrySet()) {
            Class<?> processablePacket = e.getKey();
            if (processablePacket.equals(p.getClass()) ||
                    processablePacket.isInterface() && processablePacket.isAssignableFrom(p.getClass())) {
                e.getValue().process(p);
            }
        }
    }
}
