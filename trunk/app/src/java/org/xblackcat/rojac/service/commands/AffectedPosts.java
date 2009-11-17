package org.xblackcat.rojac.service.commands;

import gnu.trove.TIntHashSet;
import org.apache.commons.lang.ArrayUtils;

/**
 * @author xBlackCat
 */

public class AffectedPosts {
    private final TIntHashSet affectedMessageIds;
    private final TIntHashSet affectedForumIds;

    public AffectedPosts(int[] affectedMessageIds, int[] affectedForumIds) {
        this.affectedMessageIds = new TIntHashSet(affectedMessageIds);
        this.affectedForumIds = new TIntHashSet(affectedForumIds);
    }

    public AffectedPosts(TIntHashSet affectedMessageIds, TIntHashSet affectedForumIds) {
        this.affectedMessageIds = affectedMessageIds;
        this.affectedForumIds = affectedForumIds;
    }

    public AffectedPosts() {
        this(ArrayUtils.EMPTY_INT_ARRAY, ArrayUtils.EMPTY_INT_ARRAY);
    }

    public int[] getAffectedMessageIds() {
        return affectedMessageIds.toArray();
    }

    public int[] getAffectedForumIds() {
        return affectedForumIds.toArray();
    }

    public boolean isContainsMessage(int messageId) {
        return affectedMessageIds.contains(messageId);
    }

    public boolean isContainsForum(int forumId) {
        return affectedForumIds.contains(forumId);
    }

    public AffectedPosts merge(AffectedPosts... pp) {
        TIntHashSet mids = new TIntHashSet();
        TIntHashSet fids = new TIntHashSet();

        mids.addAll(affectedMessageIds.toArray());
        fids.addAll(affectedForumIds.toArray());

        for (AffectedPosts p : pp) {
            mids.addAll(p.affectedMessageIds.toArray());
            fids.addAll(p.affectedForumIds.toArray());
        }

        return new AffectedPosts(mids, fids);
    }
}
