package org.xblackcat.rojac.gui.dialog.subscribtion;

import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.janus.commands.Request;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author xBlackCat
 */
class UpdateForumListAction extends AButtonAction {
    private Window window;

    public UpdateForumListAction(Window window) {
        super(Message.View_Forums_Button_Update);
        this.window = window;
    }

    public void actionPerformed(ActionEvent e) {
        Request.GET_FORUMS_LIST.process(window);
    }
}
