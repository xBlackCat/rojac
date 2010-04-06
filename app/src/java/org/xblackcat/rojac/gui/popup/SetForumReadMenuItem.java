package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @author xBlackCat
*/
class SetForumReadMenuItem extends JMenuItem {
    public SetForumReadMenuItem(Messages text, final int forumId, final boolean readFlag) {
        super(text.get());
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ForumReadUpdater(forumId, readFlag).execute();
            }
        });
    }
}
