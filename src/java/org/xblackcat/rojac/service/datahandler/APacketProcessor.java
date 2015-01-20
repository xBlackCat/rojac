package org.xblackcat.rojac.service.datahandler;

import java.lang.reflect.ParameterizedType;

/**
 * @author xBlackCat
 */

public abstract class APacketProcessor<T extends IPacket> {
    private final Class<T> packetTarget;

    public APacketProcessor() {
        this.packetTarget = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public abstract void process(T p);

    public final boolean canProcess(IPacket p) {
        return packetTarget.equals(p.getClass()) || packetTarget.isAssignableFrom(p.getClass());
    }
}
