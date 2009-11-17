package org.xblackcat.rojac.gui;

import ch.lambdaj.function.aggregate.Aggregator;
import gnu.trove.TIntHashSet;
import org.xblackcat.rojac.service.commands.AffectedIds;

/**
* @author xBlackCat
*/
class AffectedPostsAggregator implements Aggregator<AffectedIds> {
    @Override
    public AffectedIds aggregate(Iterable<? extends AffectedIds> iterable) {
        TIntHashSet mIds = new TIntHashSet();
        TIntHashSet fIds = new TIntHashSet();

        for (AffectedIds p : iterable) {
            mIds.addAll(p.getMessageIds());
            fIds.addAll(p.getForumIds());
        }

        return new AffectedIds(mIds, fIds);
    }
}
