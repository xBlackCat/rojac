package org.xblackcat.rojac.service.janus.commands;

import gnu.trove.TIntHashSet;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectProcedure;
import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.gui.view.thread.ITreeItem;

/**
 * @author xBlackCat
 */

public class AffectedIds {
    public static final int DEFAULT_FORUM = 0;
    private final TIntObjectHashMap<TIntHashSet> messageByForums;

    public AffectedIds() {
        this(new TIntObjectHashMap<TIntHashSet>());
    }

    public AffectedIds(TIntObjectHashMap<TIntHashSet> messageByForums) {
        this.messageByForums = messageByForums;
    }

    public void addMessageId(int messageId) {
        addMessageId(DEFAULT_FORUM, messageId);
    }

    public void addItem(ITreeItem<?> item) {
        addMessageId(item.getForumId(), item.getMessageId());
    }
    
    public void addMessageId(int forumId, int messageId) {
        if (messageByForums.containsKey(forumId)) {
            messageByForums.get(forumId).add(messageId);
        } else {
            messageByForums.put(forumId, new TIntHashSet(new int[] {messageId}));
        }
    }

    public void addForumId(int forumId) {
        if (!messageByForums.containsKey(forumId)) {
            messageByForums.put(forumId, new TIntHashSet());
        }
    }

    public int[] getMessageIds(int forumId) {
        TIntHashSet messageIds = messageByForums.get(forumId);
        if (messageIds != null && !messageIds.isEmpty()) {
            return messageIds.toArray();
        } else {
            return ArrayUtils.EMPTY_INT_ARRAY;
        }
    }

    public int[] getMessageIds() {
        return collectMessageIds().toArray();
    }

    private TIntHashSet collectMessageIds() {
        final TIntHashSet messages = new TIntHashSet();

        messageByForums.forEachValue(new TObjectProcedure<TIntHashSet>() {
            @Override
            public boolean execute(TIntHashSet ids) {
                messages.addAll(ids.toArray());
                return true;
            }
        });
        return messages;
    }

    public int[] getForumIds() {
        return messageByForums.keys();
    }

    /**
     * Checks if the message id is stored for specified forum or default group.
     * @param forumId
     * @param messageId
     * @return
     */
    public boolean containsMessage(int forumId, int messageId) {
        TIntHashSet messageIds = messageByForums.get(forumId);
        if (messageIds != null && messageIds.contains(messageId)) {
            return true;
        }

        messageIds = messageByForums.get(DEFAULT_FORUM);
        return messageIds != null && messageIds.contains(messageId);
    }

    public boolean containsMessage(int messageId) {
        return collectMessageIds().contains(messageId);
    }

    public boolean containsForum(int forumId) {
        return messageByForums.contains(forumId);
    }

    @Override
    public String toString() {
        return "Affected ids[count: message ids = "+ collectMessageIds().size() + ", forum ids = " + messageByForums.size() + "]";
    }

    public void clear() {
        messageByForums.clear();
    }
}
