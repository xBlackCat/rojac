package org.xblackcat.rojac.service.janus.commands;

import ch.lambdaj.function.aggregate.Aggregator;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntObjectHashMap;

import java.util.Iterator;

/**
* @author xBlackCat
*/
class AffectedPostsAggregator implements Aggregator<AffectedIds> {
    @Override
    public AffectedIds aggregate(Iterator<? extends AffectedIds> iterator) {
        TIntObjectHashMap<TIntHashSet> mIds = new TIntObjectHashMap<TIntHashSet>();

        while (iterator.hasNext()) {
            AffectedIds p = iterator.next();
            for (int forumId : p.getForumIds()) {                
                if (mIds.containsKey(forumId)) {
                    mIds.get(forumId).addAll(p.getMessageIds(forumId));
                } else {
                    mIds.put(forumId, new TIntHashSet(p.getMessageIds(forumId)));
                }
            }
        }

        return new AffectedIds(mIds);
    }
}
