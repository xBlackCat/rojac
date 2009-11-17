package org.xblackcat.rojac.service.commands;

import gnu.trove.TIntHashSet;
import org.apache.commons.lang.ArrayUtils;

/**
 * @author xBlackCat
 */

public class AffectedIds {
    private final TIntHashSet messageIds;
    private final TIntHashSet forumIds;

    public AffectedIds(int[] messageIds, int[] forumIds) {
        this.messageIds = new TIntHashSet(messageIds);
        this.forumIds = new TIntHashSet(forumIds);
    }

    public AffectedIds(TIntHashSet messageIds, TIntHashSet forumIds) {
        this.messageIds = messageIds;
        this.forumIds = forumIds;
    }

    public AffectedIds() {
        this(ArrayUtils.EMPTY_INT_ARRAY, ArrayUtils.EMPTY_INT_ARRAY);
    }

    public int[] getMessageIds() {
        return messageIds.toArray();
    }

    public int[] getForumIds() {
        return forumIds.toArray();
    }

    public boolean isContainsMessage(int messageId) {
        return messageIds.contains(messageId);
    }

    public boolean isContainsForum(int forumId) {
        return forumIds.contains(forumId);
    }

    @Override
    public String toString() {
        return "Affected ids[count: message ids = "+ messageIds.size() + ", forum ids = " + forumIds.size() + "]";
    }
}
