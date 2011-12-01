package org.xblackcat.rojac.gui.view.message;

import org.xblackcat.rojac.data.MessageData;

/**
* 01.12.11 17:16
*
* @author xBlackCat
*/
class MessageDataHolder {
    private final MessageData message;
    private final String messageBody;

    MessageDataHolder(MessageData message, String messageBody) {
        this.message = message;
        this.messageBody = messageBody;
    }

    public MessageData getMessage() {
        return message;
    }

    public String getMessageBody() {
        return messageBody;
    }
}
