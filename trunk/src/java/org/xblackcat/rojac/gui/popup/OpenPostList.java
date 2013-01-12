package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.ViewType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author xBlackCat
 */
class OpenPostList extends JMenuItem {
    private final ViewId viewId;

    public OpenPostList(int userId, final IAppControl appControl, String text, ViewType type) {
        super(text);
        viewId = type.makeId(userId);

        addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                appControl.openTab(viewId);
            }
        });
    }
}
