package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.view.thread.Post;
import org.xblackcat.rojac.gui.view.thread.SetMessageReadFlag;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
* @author xBlackCat
*/
class MarkMessageAction implements ActionListener {
    private final Post message;
    private final boolean readFlag;

    public MarkMessageAction(Post message, boolean readFlag) {
        this.message = message;
        this.readFlag = readFlag;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new SetMessageReadFlag(message, readFlag).execute();
    }
}
