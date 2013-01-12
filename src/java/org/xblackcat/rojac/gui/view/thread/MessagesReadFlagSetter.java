package org.xblackcat.rojac.gui.view.thread;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.datahandler.SetReadExPacket;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.Collection;


/**
 * @author xBlackCat
 */
public class MessagesReadFlagSetter extends RojacWorker<Void, MessageData> {
    private Collection<MessageData> posts;
    private boolean read;

    public MessagesReadFlagSetter(boolean read, Collection<MessageData> posts) {
        this.posts = posts;
        this.read = read;
    }

    @Override
    protected Void perform() throws Exception {
        IMessageAH messageAH = Storage.get(IMessageAH.class);

        for (MessageData post : posts) {
            messageAH.updateMessageReadFlag(post.getMessageId(), read);
        }
        return null;
    }

    @Override
    protected void done() {
        new SetReadExPacket(posts, read).dispatch();
    }
}
