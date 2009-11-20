package org.xblackcat.rojac.service.janus.commands;

import ch.lambdaj.function.aggregate.Aggregator;
import gnu.trove.TIntHashSet;

import java.util.Iterator;

/**
* @author xBlackCat
*/
class AffectedPostsAggregator implements Aggregator<AffectedIds> {
    @Override
    public AffectedIds aggregate(Iterator<? extends AffectedIds> iterator) {
        TIntHashSet mIds = new TIntHashSet();
        TIntHashSet fIds = new TIntHashSet();

        while (iterator.hasNext()) {
            AffectedIds p = iterator.next();
            mIds.addAll(p.getMessageIds());
            fIds.addAll(p.getForumIds());
        }

        return new AffectedIds(mIds, fIds);
    }
}
