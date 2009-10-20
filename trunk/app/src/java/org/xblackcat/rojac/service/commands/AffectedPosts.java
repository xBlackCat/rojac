package org.xblackcat.rojac.service.commands;

import gnu.trove.TIntHashSet;
import org.apache.commons.lang.ArrayUtils;

/**
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

    public AffectedPosts merge(AffectedPosts... pp) {
        TIntHashSet mids = new TIntHashSet(affectedMessageIds);
        TIntHashSet fids = new TIntHashSet(affectedForumIds);

        for (AffectedPosts p : pp) {
            mids.addAll(p.affectedMessageIds);
            fids.addAll(p.affectedForumIds);
        }

        return new AffectedPosts(mids.toArray(), fids.toArray());
    }
}
