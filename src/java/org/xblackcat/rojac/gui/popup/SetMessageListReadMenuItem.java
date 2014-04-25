package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.thread.MessagesReadFlagSetter;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author xBlackCat
 */
class SetMessageListReadMenuItem extends JMenuItem {
    public SetMessageListReadMenuItem(Message text, final Post root, final boolean read) {
        super(text.get());
        this.addActionListener(
                e -> {
                    Collection<MessageData> datas = new LinkedList<>();

                    for (Post p : root.getSubTreeFlatten()) {
                        datas.add(p.getMessageData());
                    }

                    new MessagesReadFlagSetter(read, datas).execute();
                }
        );
    }
}
