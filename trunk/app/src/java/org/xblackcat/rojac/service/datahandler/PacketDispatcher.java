package org.xblackcat.rojac.service.datahandler;

import org.xblackcat.rojac.util.RojacUtils;

/**
 * Util class to handle packets dispatching classes.
 *
 * @author xBlackCat
 */

public class PacketDispatcher {
    private final Class<IPacket>[] packetClasses;
    private final IPacketProcessor<IPacket>[] processors;

    @SuppressWarnings({"unchecked"})
    public PacketDispatcher(IPacketProcessor<IPacket>... processors) {
        if (processors == null) {
            processors = new IPacketProcessor[0];
        }
        this.processors = processors;
        packetClasses = (Class<IPacket>[]) new Class[processors.length];

        for (int i = 0; i < processors.length; i++) {
            if (processors[i] != null) {
                packetClasses[i] = RojacUtils.getGenericClass(processors[i].getClass());
            }
        }
    }

    public void dispatch(IPacket p) {
        for (int i = 0; i < packetClasses.length; i++) {
            if (packetClasses[i] == p.getClass() ||
                    packetClasses[i].getClass().isAssignableFrom(p.getClass())) {
                processors[i].process(p);
            }
        }
    }
}
