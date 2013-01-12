package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                new UserIgnoreFlagSetter(ignored, userId).execute();
            }
        });
    }

}
