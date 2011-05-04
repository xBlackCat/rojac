package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.thread.MessageReadFlagSetter;
import org.xblackcat.rojac.gui.view.thread.MessagesReadFlagSetter;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author xBlackCat
 */
class SetMessageListReadMenuItem extends JMenuItem {
    public SetMessageListReadMenuItem(Messages text, final Post root, final boolean read) {
        super(text.get());
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Collection<MessageData> datas = new LinkedList<MessageData>();

                for (Post p : root.getChildren()) {
                    datas.add(p.getMessageData());
                }

                new MessagesReadFlagSetter(read, datas).execute();
            }
        });
    }
}