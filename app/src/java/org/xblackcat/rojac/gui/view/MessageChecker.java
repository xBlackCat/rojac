package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessageData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacWorker;

/**
 * @author xBlackCat
 */

public class MessageChecker extends RojacWorker<Void, MessageData> {
    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    protected MessageData data;
    protected final int messageId;

    public MessageChecker(int messageId) {
        this.messageId = messageId;
    }

    @Override
    protected Void perform() throws Exception {
        if (messageId > 0) {
            data = storage.getMessageAH().getMessageData(messageId);
        } else {
            data = new NewMessageData(storage.getNewMessageAH().getNewMessageById(-messageId));
        }
        return null;
    }
}
