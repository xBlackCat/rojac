package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.view.thread.AThreadModel;
import org.xblackcat.rojac.gui.view.thread.ITreeItem;
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

    /**
     * Creates a sub-menu with open-message actions. The actions are: <ul> <li>Open the message. If the message is in
     * one of opened views, the view will be moved for front and the message will be selected in it. If correspond forum
     * view is not opened it will be opened.</li> <li>Open the message in the current message view.</li> <li>Open
     * message view in a separate tab.</li> </ul>
     *
     * @param messageId target message id.
     * @param mainFrame link to common frame.
     *
     * @return sub-menu with registered actions to open message in various ways.
     */
    static JMenu openMessageSubmenu(int messageId, IRootPane mainFrame) {
        JMenu menu = new JMenu();
        menu.setText("Open message");

        JMenuItem open = new JMenuItem();
        open.setText("Open message");
        open.addActionListener(new OpenMessageAction(mainFrame, messageId, OpenMessageMethod.Default));
        menu.add(open);

        JMenuItem openHere = new JMenuItem();
        openHere.setText("Open message in current view");
        openHere.addActionListener(new OpenMessageAction(mainFrame, messageId, OpenMessageMethod.ThisView));
        menu.add(openHere);

        JMenuItem openNewTab = new JMenuItem();
        openNewTab.setText("Open in new tab");
        openNewTab.addActionListener(new OpenMessageAction(mainFrame, messageId, OpenMessageMethod.NewTab));
        menu.add(openNewTab);

        return menu;
    }

    /**
     * Adds to an menu a 'open in browser' action if it supported by current OS.
     *
     * @param menu target menu to be filled with the menu item.
     * @param url
     */
    static void addOpenLink(JPopupMenu menu, String url) {
        final Desktop desktop = Desktop.getDesktop();

        if (url != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            JMenuItem openInBrowserItem = new JMenuItem(Messages.POPUP_LINK_OPEN_IN_BROWSER.get());
            openInBrowserItem.addActionListener(new OpenUrlAction(url));

            menu.add(openInBrowserItem);
        }
    }

    static JMenuItem copyToClipboard(String url) {
        JMenuItem copyToClipboard = new JMenuItem(Messages.POPUP_LINK_COPY_TO_CLIPBOARD.get());
        copyToClipboard.addActionListener(new CopyUrlAction(url));

        return copyToClipboard;
    }

    /**
     * Builds menu action "set item as read"
     *
     * @param message
     * @param model
     *@param mainFrame
     *  @return
     */
    public static JMenuItem markRead(ITreeItem message, AThreadModel<? extends ITreeItem<?>> model, IRootPane mainFrame) {
        return null;
    }

    public static JMenuItem markUnread(ITreeItem message, AThreadModel<? extends ITreeItem<?>> model, IRootPane mainFrame) {
        return null;
    }

    public static JMenuItem markReadSubmenu(ITreeItem message, AThreadModel<? extends ITreeItem<?>> model, IRootPane mainFrame) {
        return null;
    }

    public static JMenuItem markUnreadSubmenu(ITreeItem message, AThreadModel<? extends ITreeItem<?>> model, IRootPane mainFrame) {
        return null;
    }
}
