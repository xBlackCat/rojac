package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */
class OpenMessageMenuItem extends JMenuItem {
    public OpenMessageMenuItem(final int messageId, final IAppControl appControl, Message text, final OpenMessageMethod type) {
        setText(text.get());
        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                appControl.openMessage(messageId, type);
            }
        });
    }
}
