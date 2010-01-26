package org.xblackcat.rojac.service.janus.commands;

import ch.lambdaj.function.aggregate.Aggregator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
* @author xBlackCat
*/
class AffectedMessagesAggregator implements Aggregator<AffectedMessage[]> {
    @Override
    public AffectedMessage[] aggregate(Iterator<? extends AffectedMessage[]> iterator) {
        Set<AffectedMessage> result = new HashSet<AffectedMessage>();

        while (iterator.hasNext()) {
            result.addAll(Arrays.asList(iterator.next()));
        }

        return result.toArray(new AffectedMessage[result.size()]);
    }
}
