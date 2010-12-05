package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
* @author xBlackCat
*/
class OpenForumAction extends AbstractAction {
    private final IRootPane rootPane;
    private final int forumId;

    public OpenForumAction(IRootPane rootPane, int forumId) {
        super(Messages.Popup_View_Forums_Open.get());
        this.rootPane = rootPane;
        this.forumId = forumId;
    }

    public void actionPerformed(ActionEvent e) {
        rootPane.openForumTab(forumId);
    }
}
