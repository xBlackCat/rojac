package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.view.thread.MessageReadFlagSetter;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */
class SetMessageReadMenuItem extends JMenuItem {
    public SetMessageReadMenuItem(Messages text, final MessageData messageData, final boolean read) {
        super(text.get());
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MessageReadFlagSetter(read, messageData).execute();
            }
        });
    }
}
