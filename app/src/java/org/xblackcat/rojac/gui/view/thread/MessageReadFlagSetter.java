package org.xblackcat.rojac.gui.view.thread;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.datahandler.SetPostReadPacket;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;


/**
 * @author xBlackCat
 */
public class MessageReadFlagSetter extends RojacWorker<Void, Void> {
    private static final TIntSet messageToQuery = new TIntHashSet();

    private final MessageData post;
    private final boolean read;

    public MessageReadFlagSetter(boolean read, MessageData data) {
        this.post = data;
        this.read = read;

        synchronized (messageToQuery) {
            messageToQuery.add(post.getMessageId());
        }
    }

    @Override
    protected Void perform() throws Exception {
        final int messageId = post.getMessageId();
        synchronized (messageToQuery) {
            if (!messageToQuery.contains(messageId)) {
                // Already dispatched
                return null;
            }
        }

        if (post.isRead() != read) {
            Storage.get(IMessageAH.class).updateMessageReadFlag(messageId, read);

            publish();
        }
        return null;
    }

    @Override
    protected void process(List<Void> chunks) {
        new SetPostReadPacket(post, read).dispatch();

        synchronized (messageToQuery) {
            messageToQuery.remove(post.getMessageId());
        }
    }
}
