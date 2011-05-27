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
public class ThreadReadFlagSetter extends RojacWorker<Void, Void> {
    private final boolean read;
    private final MessageData threadRoot;

    public ThreadReadFlagSetter(boolean read, MessageData messageData) {
        this.read = read;
        this.threadRoot = messageData;
    }

    @Override
    protected Void perform() throws Exception {
        IStorage storage = ServiceFactory.getInstance().getStorage();
        storage.getMessageAH().updateThreadReadFlag(threadRoot.getMessageId(), read);
        return null;
    }

    @Override
    protected void done() {
        IPacket packet = new SetPostReadPacket(read, threadRoot.getForumId(), threadRoot.getMessageId(), true);
        ServiceFactory.getInstance().getDataDispatcher().processPacket(packet);
    }
}
