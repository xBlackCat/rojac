package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.thread.MessageReadFlagSetter;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */
class SetItemReadMenuItem extends JMenuItem {
    public SetItemReadMenuItem(Messages text, final Post post, final boolean read) {
        super(text.get());
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MessageReadFlagSetter(read, post.getMessageData()).execute();
            }
        });
    }
}