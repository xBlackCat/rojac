package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.janus.commands.Request;

import java.awt.*;
import java.awt.event.ActionEvent;

/**
* @author xBlackCat
*/
public class UpdateForumListAction extends AButtonAction {
    private Window window;

    public UpdateForumListAction(Window window) {
        super(Messages.View_Forums_Button_Update);
        this.window = window;
    }

    public void actionPerformed(ActionEvent e) {
        Request.GET_FORUMS_LIST.process(window);
    }
}
