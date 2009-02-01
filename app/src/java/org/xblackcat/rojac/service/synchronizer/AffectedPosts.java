package org.xblackcat.rojac.service.synchronizer;

import org.apache.commons.lang.ArrayUtils;

/**
 * Date: 7 זמגע 2008
 *
 * @author xBlackCat
 */

public class AffectedPosts {
    private final int[] affectedMessageIds;
    private final int[] affectedForumIds;

    public AffectedPosts(int[] affectedMessageIds, int[] affectedForumIds) {
        this.affectedMessageIds = affectedMessageIds;
        this.affectedForumIds = affectedForumIds;
    }

    public AffectedPosts() {
        this(ArrayUtils.EMPTY_INT_ARRAY, ArrayUtils.EMPTY_INT_ARRAY);
    }

    public int[] getAffectedMessageIds() {
        return affectedMessageIds;
    }

    public int[] getAffectedForumIds() {
        return affectedForumIds;
    }
}
