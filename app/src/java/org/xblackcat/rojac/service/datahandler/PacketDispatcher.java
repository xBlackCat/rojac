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
    private final Map<Class<?>, IPacketProcessor<IPacket>> processors = new HashMap<Class<?>, IPacketProcessor<IPacket>>();

    @SuppressWarnings({"unchecked"})
    public PacketDispatcher(IPacketProcessor<IPacket>... processors) {
        if (processors == null) {
            return;
        }

        for (IPacketProcessor<IPacket> processor : processors) {
            if (processor != null) {
                this.processors.put(RojacUtils.getGenericClass(processor.getClass()), processor);
            }
        }
    }

    public void dispatch(IPacket p) {
        IPacketProcessor<IPacket> processor = processors.get(p.getClass());

        if (processor != null) {
            processor.process(p);
        } else {
            for (Map.Entry<Class<?>, IPacketProcessor<IPacket>> e : processors.entrySet()) {
                if (e.getKey().isAssignableFrom(p.getClass())) {
                    e.getValue().process(p);
                }
            }
        }
    }
}
