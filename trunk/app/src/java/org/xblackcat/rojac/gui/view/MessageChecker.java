package org.xblackcat.rojac.gui.view;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.NewMessageData;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

/**
 * @author xBlackCat
 */

public class MessageChecker extends RojacWorker<Void, MessageData> {
    protected MessageData data;
    protected final int messageId;

    public MessageChecker(int messageId) {
        this.messageId = messageId;
    }

    @Override
    protected Void perform() throws Exception {
        if (messageId > 0) {
            data = Storage.get(IMessageAH.class).getMessageData(messageId);
        } else {
            data = new NewMessageData(Storage.get(INewMessageAH.class).getNewMessageById(-messageId));
        }
        return null;
    }
}
