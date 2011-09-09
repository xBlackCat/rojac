package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.SetPostReadPacket;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;


/**
 * @author xBlackCat
 */
public class MessageReadFlagSetter extends RojacWorker<Void, Void> {
    private MessageData post;
    private boolean read;

    public MessageReadFlagSetter(boolean read, MessageData data) {
        this.post = data;
        this.read = read;
    }

    @Override
    protected Void perform() throws Exception {
        Storage.get(IMessageAH.class).updateMessageReadFlag(post.getMessageId(), read);
        return null;
    }

    @Override
    protected void done() {
        if (post.isRead() != read) {
            IPacket processPacket = new SetPostReadPacket(
                    post, read
            );
            processPacket.dispatch();
        }
    }
}
