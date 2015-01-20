package org.xblackcat.rojac.service.datahandler;

/**
 * Util class to handle packets dispatching classes.
 *
 * @author xBlackCat
 */

public class PacketDispatcher {
    private final APacketProcessor<? extends IPacket>[] processors;

    @SafeVarargs
    public PacketDispatcher(APacketProcessor<? extends IPacket>... processors) {
        this.processors = processors;
    }

    public void dispatch(IPacket p) {
        for (APacketProcessor<? extends IPacket> e : processors) {
            if (e.canProcess(p)) {
                ((APacketProcessor) e).process(p);
            }
        }
    }
}
