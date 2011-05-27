package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public interface IPacketProcessor<T extends IPacket> {
    void process(T p);
}
