package org.xblackcat.rojac.service.datahandler;

import org.apache.commons.lang.ArrayUtils;

/**
 * @author xBlackCat
 */

public class ForumsUpdatePacket implements IForumUpdatePacket {
    private final int[] forumIds;

    public ForumsUpdatePacket(int... forumIds) {
        this.forumIds = forumIds;
    }

    @Override
    public int[] getForumIds() {
        return forumIds;
    }

    @Override
    public boolean isForumAffected(int forumId) {
        return ArrayUtils.contains(forumIds, forumId);
    }

}
