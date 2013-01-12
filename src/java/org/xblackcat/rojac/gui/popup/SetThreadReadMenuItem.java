package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.model.Thread;
import org.xblackcat.rojac.gui.view.thread.SubTreeReadFlagSetter;
import org.xblackcat.rojac.gui.view.thread.ThreadReadFlagSetter;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */
class SetThreadReadMenuItem extends JMenuItem {
    public SetThreadReadMenuItem(Message text, final Post post, final boolean read) {
        super(text.get());
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (post instanceof Thread) {
                    // Mark whole thread.
                    new ThreadReadFlagSetter(read, post.getMessageData()).execute();
                } else {
                    // Mark sub-tree as read
                    new SubTreeReadFlagSetter(read, post).execute();
                }
            }
        });
    }

    public SetThreadReadMenuItem(Message text, final MessageData data, final boolean read) {
        super(text.get());
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mark whole thread.
                new ThreadReadFlagSetter(read, data).execute();
            }
        });
    }
}
