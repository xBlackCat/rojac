package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;

/**
 * @author xBlackCat
 */
class SetForumReadMenuItem extends JMenuItem {
    public SetForumReadMenuItem(Message text, final int forumId, final boolean readFlag) {
        super(text.get());
        addActionListener(e -> new ForumReadUpdater(forumId, readFlag).execute());
    }
}
