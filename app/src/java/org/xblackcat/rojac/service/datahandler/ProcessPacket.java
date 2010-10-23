package org.xblackcat.rojac.service.datahandler;

import gnu.trove.TIntHashSet;
import gnu.trove.TIntObjectHashMap;
import gnu.trove.TObjectProcedure;
import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.gui.view.thread.ITreeItem;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;
import org.xblackcat.rojac.util.PacketUtils;

import java.util.Collection;

/**
 * @author xBlackCat
 */

public final class ProcessPacket {
    /**
     * The constant is used for preventing generating large amount of empty HashSet objects.
     */
    private static final TIntHashSet EMPTY_FORUM = new TIntHashSet();
    private final TIntObjectHashMap<TIntHashSet> messageByForums = new TIntObjectHashMap<TIntHashSet>();
    private final PacketType type;

    public ProcessPacket(PacketType type) {
        this.type = type;
    }

    public ProcessPacket(PacketType type, Collection<AffectedMessage> messages) {
        this.type = type;
        for (AffectedMessage am : messages) {
            int forumId = am.getForumId();
            if (!messageByForums.containsKey(forumId)) {
                messageByForums.put(forumId, new TIntHashSet());
            }

            Integer messageId = am.getMessageId();
            if (messageId != null) {
                messageByForums.get(forumId).add(messageId);
            }
        }
    }

    public ProcessPacket(PacketType type, int... forumIds) {
        this.type = type;
        for (int forumId : forumIds) {
            messageByForums.put(forumId, EMPTY_FORUM);
        }
    }

    public ProcessPacket(PacketType type, ITreeItem<?> ... items) {
        this(type, PacketUtils.toAffectedMessages(items));
    }

    public PacketType getType() {
        return type;
    }

    public AffectedMessage[] getAffectedMessages(int forumId) {
        TIntHashSet messageIds = messageByForums.get(forumId);
        if (messageIds != null && !messageIds.isEmpty()) {
            return PacketUtils.makeAffectedMessages(forumId, messageIds.toArray());
        } else {
            return AffectedMessage.EMPTY;
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

        messageIds = messageByForums.get(AffectedMessage.DEFAULT_FORUM);
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
}
