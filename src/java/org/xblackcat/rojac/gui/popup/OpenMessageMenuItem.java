package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;

/**
 * @author xBlackCat
 */
class OpenMessageMenuItem extends JMenuItem {
    public OpenMessageMenuItem(final int messageId, final IAppControl appControl, Message text, final OpenMessageMethod type) {
        setText(text.get());
        addActionListener(e -> appControl.openMessage(messageId, type));
    }
}
