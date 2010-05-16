package org.xblackcat.rojac.gui.popup;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.thread.AThreadModel;
import org.xblackcat.rojac.gui.view.thread.ITreeItem;
import org.xblackcat.rojac.gui.view.thread.Post;

import javax.swing.*;

/** @author xBlackCat */

class TreeThreadViewPopupBuilder implements IPopupBuilder {
    @Override
    public JPopupMenu buildMenu(Object... parameters) {
        if (ArrayUtils.isEmpty(parameters) || parameters.length != 3) {
            throw new IllegalArgumentException("Invalid parameters amount.");
        }

        Post message = (Post) parameters[0];
        AThreadModel<? extends ITreeItem<?>> model = (AThreadModel<?>) parameters[1];
        IRootPane mainFrame = (IRootPane) parameters[2];

        return buildInternal(message, model, mainFrame);
    }

    /**
     * Real menu builder.
     *
     * @param message message data to build menu.
     * @param model
     *@param mainFrame main frame controller
     *  @return built pop-up menu.
     */
    private JPopupMenu buildInternal(Post message, AThreadModel<? extends ITreeItem<?>> model, IRootPane mainFrame) {
        int messageId = message.getMessageId();
        final JPopupMenu menu = new JPopupMenu("#" + messageId);

        JMenuItem item = new JMenuItem("#" + messageId);
        item.setEnabled(false);

        menu.add(item);

        menu.add(MenuHelper.openMessageSubmenu(messageId, mainFrame));
        menu.addSeparator();

        menu.add(MenuHelper.markRead(message, model, mainFrame));
        menu.add(MenuHelper.markUnread(message, model, mainFrame));

        menu.add(MenuHelper.markReadUnreadSubmenu(message, model, mainFrame));

        menu.addSeparator();
        menu.add(MenuHelper.copyLinkSubmenu(messageId));


        return menu;
    }
}
