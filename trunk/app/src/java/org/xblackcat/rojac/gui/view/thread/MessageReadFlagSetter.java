package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.SetPostReadPacket;
import org.xblackcat.rojac.service.storage.IStorage;
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
        IStorage storage = ServiceFactory.getInstance().getStorage();
        storage.getMessageAH().updateMessageReadFlag(post.getMessageId(), read);
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
