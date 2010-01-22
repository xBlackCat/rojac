package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.OpenMessageMethod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple implementation to make "open-message" action.
 *
 * @author xBlackCat
 */
class OpenMessageAction implements ActionListener {
    private final IRootPane mainFrame;
    private final int messageId;
    protected OpenMessageMethod openMessageMethod;

    public OpenMessageAction(IRootPane mainFrame, int messageId, OpenMessageMethod openMessageMethod) {
        this.mainFrame = mainFrame;
        this.messageId = messageId;
        this.openMessageMethod = openMessageMethod;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        mainFrame.openMessage(messageId, openMessageMethod);
    }
}
