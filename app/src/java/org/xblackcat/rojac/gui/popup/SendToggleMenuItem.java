package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.IgnoreUpdatedPacket;
import org.xblackcat.rojac.service.datahandler.NewMessagesUpdatedPacket;
import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author xBlackCat
 */
class SendToggleMenuItem extends JMenuItem {
    public SendToggleMenuItem(MessageData newMessage) {
        final int messageId = -newMessage.getMessageId();
        final boolean draft = !newMessage.isRead();

        Message message = draft ?
                Message.Popup_View_OutboxTree_MarkToSend :
                Message.Popup_View_OutboxTree_MarkAsDraft;

        setText(message.get());
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RojacWorker<Void, Void>() {
                    @Override
                    protected Void perform() throws Exception {
                        Storage.get(INewMessageAH.class).setDraftFlag(!draft, messageId);

                        publish();

                        return null;
                    }

                    @Override
                    protected void process(List<Void> chunks) {
                        new NewMessagesUpdatedPacket().dispatch();
                    }
                }.execute();
            }
        });
    }
}
