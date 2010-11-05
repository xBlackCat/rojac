package org.xblackcat.rojac.service.datahandler;

/**
 * @author xBlackCat
 */

public interface IForumUpdatePacket extends IPacket {
    int[] getForumIds();

    boolean isForumAffected(int forumId);
}
