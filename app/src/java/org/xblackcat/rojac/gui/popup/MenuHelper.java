package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.view.thread.Post;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.LinkUtils;

import javax.swing.*;
import java.awt.*;

/** @author xBlackCat */

final class MenuHelper {
    private MenuHelper() {
    }

    /**
     * Creates a sub-menu with copy-message-link actions. The actions are: <ul> <li>Copy link to the message</li>
     * <li>Copy link to whole thread. The thread layout is flat.</li> <li>Copy link to whole thread. The thread layout
     * is tree.</li> </ul>
     *
     * @param messageId message id to be used as base of target URLs.
     *
     * @return sub-menu with registered actions to copy various URLs.
     */
    static JMenu copyLinkSubmenu(int messageId) {
        /* Copy link sub-menu. */

        JMenu menu = new JMenu();
        menu.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL.get());

        JMenuItem copyUrl = new JMenuItem();
        copyUrl.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL_MESSAGE.get());
        copyUrl.addActionListener(new CopyUrlAction(LinkUtils.buildMessageLink(messageId)));
        menu.add(copyUrl);

        JMenuItem copyFlatUrl = new JMenuItem();
        copyFlatUrl.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL_FLAT.get());
        copyFlatUrl.addActionListener(new CopyUrlAction(LinkUtils.buildFlatThreadLink(messageId)));
        menu.add(copyFlatUrl);

        JMenuItem copyThreadUrl = new JMenuItem();
        copyThreadUrl.setText(Messages.POPUP_VIEW_THREADS_TREE_COPYURL_THREAD.get());
        copyThreadUrl.addActionListener(new CopyUrlAction(LinkUtils.buildThreadLink(messageId)));
        menu.add(copyThreadUrl);
        return menu;
    }

    static JMenuItem openMessage(int messageId, IRootPane mainFrame) {
        JMenuItem open = new JMenuItem();
        open.setText(Messages.POPUP_VIEW_THREADS_TREE_OPEN_MESSAGE.get());
        open.addActionListener(new OpenMessageAction(mainFrame, messageId, OpenMessageMethod.Default));
        return open;        
    }

    static JMenuItem openMessageInTab(int messageId, IRootPane mainFrame) {
        JMenuItem open = new JMenuItem();
        open.setText(Messages.POPUP_VIEW_THREADS_TREE_OPEN_MESSAGE_NEW_TAB.get());
        open.addActionListener(new OpenMessageAction(mainFrame, messageId, OpenMessageMethod.NewTab));
        return open;
    }

    /**
     * Adds to an menu a 'open in browser' action if it supported by current OS.
     *
     * @param menu target menu to be filled with the menu item.
     * @param linkMessage
     * @param url
     */
    static void addOpenLink(JPopupMenu menu, Messages linkMessage, String url) {
        final Desktop desktop = Desktop.getDesktop();

        if (url != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            JMenuItem openInBrowserItem = new JMenuItem(linkMessage.get());
            openInBrowserItem.addActionListener(new OpenUrlAction(url));

            menu.add(openInBrowserItem);
        }
    }

    static JMenuItem copyToClipboard(String url) {
        JMenuItem copyToClipboard = new JMenuItem(Messages.POPUP_LINK_COPY_TO_CLIPBOARD.get());
        copyToClipboard.addActionListener(new CopyUrlAction(url));

        return copyToClipboard;
    }

    static JMenuItem markReadUnreadSubmenu(Post message) {
        JMenu menu = new JMenu();
        menu.setText(Messages.POPUP_VIEW_THREADS_TREE_MARK_TITLE.get());

        JMenuItem markThreadRead = new JMenuItem();
        markThreadRead.setText(Messages.POPUP_VIEW_THREADS_TREE_MARK_THREAD_READ.get());
        // TODO: add action
        menu.add(markThreadRead);

        JMenuItem markThreadUnread = new JMenuItem();
        markThreadUnread.setText(Messages.POPUP_VIEW_THREADS_TREE_MARK_THREAD_UNREAD.get());
        // TODO: add action
        menu.add(markThreadUnread);

        menu.addSeparator();

        menu.add(new SetForumReadMenuItem(Messages.POPUP_VIEW_FORUMS_SET_READ_ALL, message.getForumId(), true));
        menu.add(new SetForumReadMenuItem(Messages.POPUP_VIEW_FORUMS_SET_UNREAD_ALL, message.getForumId(), false));

        return menu;
    }

}
