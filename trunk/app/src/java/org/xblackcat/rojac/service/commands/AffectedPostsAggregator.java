package org.xblackcat.rojac.service.commands;

import ch.lambdaj.function.aggregate.Aggregator;
import gnu.trove.TIntHashSet;

/**
* @author xBlackCat
*/
class AffectedPostsAggregator implements Aggregator<AffectedPosts> {
    @Override
    public AffectedPosts aggregate(Iterable<? extends AffectedPosts> iterable) {
        TIntHashSet mIds = new TIntHashSet();
        TIntHashSet fIds = new TIntHashSet();

        for (AffectedPosts p : iterable) {
            mIds.addAll(p.getAffectedMessageIds());
            fIds.addAll(p.getAffectedForumIds());
    }

        return new AffectedPosts(mIds, fIds);
    }
}
