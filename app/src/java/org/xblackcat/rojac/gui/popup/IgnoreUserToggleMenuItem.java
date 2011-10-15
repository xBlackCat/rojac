package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.IgnoreUserUpdatedPacket;
import org.xblackcat.rojac.service.storage.IMiscAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author xBlackCat
 */
class IgnoreUserToggleMenuItem extends JMenuItem {
    public IgnoreUserToggleMenuItem(MessageData post) {
        final int userId = post.getUserId();
        final boolean ignored = post.isIgnoredUser();

        Message message = ignored ? Message.Popup_IgnoreUser_Reset : Message.Popup_IgnoreUser_Set;

        setText(message.get(post.getUserName()));

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RojacWorker<Void, Void>() {
                    @Override
                    protected Void perform() throws Exception {
                        IMiscAH miscAH = Storage.get(IMiscAH.class);

                        if (ignored) {
                            miscAH.removeFromIgnoredUserList(userId);
                        } else {
                            miscAH.addToIgnoredUserList(userId);
                        }

                        publish();

                        return null;
                    }

                    @Override
                    protected void process(List<Void> chunks) {
                        new IgnoreUserUpdatedPacket(userId, !ignored).dispatch();
                    }
                }.execute();
            }
        });
    }
}
