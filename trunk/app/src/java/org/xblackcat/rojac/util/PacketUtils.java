package org.xblackcat.rojac.util;

import org.xblackcat.rojac.gui.view.thread.ITreeItem;
import org.xblackcat.rojac.service.janus.commands.AffectedMessage;

import java.util.Collection;
import java.util.LinkedList;

/**
 * @author xBlackCat
 */

public final class PacketUtils {
    private PacketUtils() {
    }

    public static AffectedMessage[] toAffectedMessages(ITreeItem<?>... items) {
        Collection<AffectedMessage> result = new LinkedList<AffectedMessage>();
        for (ITreeItem<?> i : items) {
            result.add(new AffectedMessage(i.getForumId(), i.getMessageId()));
        }
        return result.toArray(new AffectedMessage[result.size()]);
    }

    public static AffectedMessage[] makeAffectedMessages(int forumId, int... messageIds) {
        AffectedMessage[] result = new AffectedMessage[messageIds.length];

        for (int i = 0; i < messageIds.length; i++) {
            result[i] = new AffectedMessage(forumId, messageIds[i]);
        }

        return result;
    }
}
