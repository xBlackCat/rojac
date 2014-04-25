package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.gui.view.ViewType;

import javax.swing.*;

/**
 * @author xBlackCat
 */
class OpenPostList extends JMenuItem {
    private final ViewId viewId;

    public OpenPostList(int userId, final IAppControl appControl, String text, ViewType type) {
        super(text);
        viewId = type.makeId(userId);

        addActionListener(e -> appControl.openTab(viewId));
    }
}
