package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.view.thread.ITreeItem;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.PacketType;
import org.xblackcat.rojac.service.datahandler.ProcessPacket;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.util.RojacWorker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @author xBlackCat
*/
class MarkMessageAction implements ActionListener {
    private final ITreeItem message;
    private final boolean readFlag;

    public MarkMessageAction(ITreeItem message, boolean readFlag) {
        this.message = message;
        this.readFlag = readFlag;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new RojacWorker<Void, Void>() {
            @Override
            protected Void perform() throws Exception {
                IMessageAH mAH = ServiceFactory.getInstance().getStorage().getMessageAH();

                mAH.updateMessageReadFlag(message.getMessageId(), readFlag);
                return null;
            }

            @Override
            protected void done() {
                ProcessPacket updateMessage = new ProcessPacket(
                        readFlag ? PacketType.SetPostRead : PacketType.SetPostUnread,
                        message
                );
                ServiceFactory.getInstance().getDataDispatcher().processPacket(updateMessage);
            }
        }.execute();
    }
}
