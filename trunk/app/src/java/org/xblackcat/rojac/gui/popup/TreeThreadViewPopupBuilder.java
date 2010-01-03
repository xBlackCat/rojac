package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.thread.ITreeItem;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public class TreeThreadViewPopupBuilder implements IPopupBuilder {
    @Override
    public JPopupMenu buildMenu(Object... parameters) {
        if (ArrayUtils.isEmpty(parameters) || parameters.length != 2) {
            throw new IllegalArgumentException("Invalid parameters amount.");
        }

        ITreeItem message = (ITreeItem) parameters[0];
        IRootPane mainFrame = (IRootPane) parameters[1];

        int messageId = message.getMessageId();
        final JPopupMenu menu = new JPopupMenu("#" + messageId);

        JMenuItem item = new JMenuItem("#" + messageId);
        item.setEnabled(false);

        menu.add(item);

        menu.addSeparator();
        menu.add(MenuHelper.openMessage(messageId, mainFrame));

        menu.addSeparator();
        menu.add(MenuHelper.copyLinkSubmenu(messageId));


        return menu;
    }

}