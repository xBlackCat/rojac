package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;

/**
* @author xBlackCat
*/
class OpenMessageMenuItem extends JMenuItem {
    public OpenMessageMenuItem(int messageId, IAppControl appControl, Messages text, OpenMessageMethod type) {
        this(messageId, 0, appControl, text, type);
    }

    public OpenMessageMenuItem(int messageId, int rootId, IAppControl appControl, Messages text, OpenMessageMethod type) {
        setText(text.get());
        addActionListener(new OpenMessageAction(messageId, rootId, appControl, type));
    }
}
