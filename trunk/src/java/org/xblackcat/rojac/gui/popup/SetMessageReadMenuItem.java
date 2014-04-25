package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.view.thread.MessageReadFlagSetter;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;

/**
 * @author xBlackCat
 */
class SetMessageReadMenuItem extends JMenuItem {
    public SetMessageReadMenuItem(Message text, final MessageData messageData, final boolean read) {
        super(text.get());
        this.addActionListener(e -> new MessageReadFlagSetter(read, messageData).execute());
    }
}
