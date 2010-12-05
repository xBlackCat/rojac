package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IRootPane;
import org.xblackcat.rojac.gui.view.forumlist.ForumData;
import org.xblackcat.rojac.gui.view.forumlist.ForumTableModel;
import org.xblackcat.rojac.gui.view.thread.AThreadModel;
import org.xblackcat.rojac.gui.view.thread.ITreeItem;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.LinkUtils;

import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class for prepare pop-up menus for the application.
 *
 * @author xBlackCat
 */
public class PopupMenuBuilder {
    public PopupMenuBuilder() {
    }

    /**
     * Constructs a pop-up menu for link in message pane.
     *
     * @param url         URL object of the link.
     * @param stringUrl Text representing of the link.
     * @param text        Text associated with the link.
     * @param mainFrame   back link to main window control.
     *
     * @return new popup menu for the specified url.
     */
    public static JPopupMenu getLinkMenu(URL url, String stringUrl, String text, IRootPane mainFrame) {
        if (url == null) {
            // Invalid url. Try to parse it from text.

            try {
                url = new URL(text);
                // Url in the 'text' field, so assume that text in the 'description' field 
                text = stringUrl;
                stringUrl = url.toExternalForm();
            } catch (MalformedURLException e) {
                // url can not be obtained neither from text nor from description.
            }
        }

        Integer messageId = LinkUtils.getMessageId(stringUrl);
        if (messageId == null) {
            messageId = LinkUtils.getMessageId(text);
        }

        // Build menu
        final JPopupMenu menu = new JPopupMenu();

        String headerText = messageId == null ? text : "#" + messageId;
        JMenuItem headerItem = new JMenuItem(headerText);
        headerItem.setEnabled(false);
        menu.add(headerItem);

        menu.addSeparator();
        if (messageId != null) {
            menu.add(MenuHelper.openMessage(messageId, mainFrame));
            menu.add(MenuHelper.openMessageInTab(messageId, mainFrame));
            menu.addSeparator();
            MenuHelper.addOpenLink(menu, Messages.Popup_Link_Open_InBrowser_Message, stringUrl);
            MenuHelper.addOpenLink(menu, Messages.Popup_Link_Open_InBrowser_Thread, LinkUtils.buildThreadLink(messageId));

        } else {
            MenuHelper.addOpenLink(menu, Messages.Popup_Link_Open_InBrowser, stringUrl);
        }

        menu.add(MenuHelper.copyToClipboard(stringUrl));
        if (messageId != null) {
            menu.add(MenuHelper.copyLinkSubmenu(messageId));
        }

        return menu;
    }

    public static JPopupMenu getForumViewMenu(ForumData forum, ForumTableModel forumsModel, IRootPane rootPane) {
        JPopupMenu menu = new JPopupMenu(forum.getForum().getForumName());

        final boolean subscribed = forum.isSubscribed();
        final int forumId = forum.getForumId();

        menu.add(new OpenForumAction(rootPane, forumId));
        menu.addSeparator();

        menu.add(new SetForumReadMenuItem(Messages.Popup_View_Forums_SetReadAll, forumId, true));
        menu.add(new SetForumReadMenuItem(Messages.Popup_View_Forums_SetUnreadAll, forumId, false));

        menu.addSeparator();

        {
            JCheckBoxMenuItem mi = new JCheckBoxMenuItem(Messages.Popup_View_Forums_Subscribe.get(), subscribed);
            mi.addActionListener(new SubscribeChangeListener(forumId, forumsModel, subscribed));
            menu.add(mi);
        }


        return menu;
    }

    public static JPopupMenu getTreeViewPopup(ITreeItem message, AThreadModel model, IRootPane mainFrame) {
        int messageId = message.getMessageId();
        final JPopupMenu menu = new JPopupMenu("#" + messageId);

        JMenuItem item = new JMenuItem("#" + messageId);
        item.setEnabled(false);

        menu.add(item);

        menu.add(MenuHelper.openMessageInTab(messageId, mainFrame));
        menu.addSeparator();

        menu.add(new SetItemReadMenuItem(Messages.Popup_View_ThreadsTree_Mark_Read, message, true));
        menu.add(new SetItemReadMenuItem(Messages.Popup_View_ThreadsTree_Mark_Unread, message, false));

        menu.add(MenuHelper.markReadUnreadSubmenu(message, mainFrame));

        menu.addSeparator();
        MenuHelper.addOpenLink(menu, Messages.Popup_Link_Open_InBrowser_Message, LinkUtils.buildMessageLink(messageId));
        MenuHelper.addOpenLink(menu, Messages.Popup_Link_Open_InBrowser_Thread, LinkUtils.buildThreadLink(messageId));
        menu.add(MenuHelper.copyLinkSubmenu(messageId));


        return menu;
    }

}
