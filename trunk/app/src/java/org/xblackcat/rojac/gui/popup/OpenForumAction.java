package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
* @author xBlackCat
*/
class OpenForumAction extends AbstractAction {
    private final IAppControl appControl;
    private final int forumId;

    public OpenForumAction(IAppControl appControl, int forumId) {
        super(Message.Popup_View_Open.get());
        this.appControl = appControl;
        this.forumId = forumId;
    }

    public void actionPerformed(ActionEvent e) {
        appControl.openTab(ViewType.Forum.makeId(forumId));
    }
}
