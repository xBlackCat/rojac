package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.view.thread.Post;
import org.xblackcat.rojac.gui.view.thread.Thread;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @author xBlackCat
*/
class SetThreadReadMenuItem extends JMenuItem {
public SetThreadReadMenuItem(Messages text, final Post post, final boolean read) {
    super(text.get());
    this.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Thread threadRoot = post.getThreadRoot();

            if (threadRoot.equals(post)) {
                // Mark whole thread.
            } else {
                // Collect sub-items to be marked. 
            }
        }
    });
}
}
