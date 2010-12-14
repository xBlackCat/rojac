package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.gui.view.model.ITreeItem;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.util.LinkUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */

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
        menu.setText(Messages.Popup_View_ThreadsTree_CopyUrl.get());

        JMenuItem copyUrl = new JMenuItem();
        copyUrl.setText(Messages.Popup_View_ThreadsTree_CopyUrl_Message.get());
        copyUrl.addActionListener(new CopyUrlAction(LinkUtils.buildMessageLink(messageId)));
        menu.add(copyUrl);

        JMenuItem copyFlatUrl = new JMenuItem();
        copyFlatUrl.setText(Messages.Popup_View_ThreadsTree_CopyUrl_Flat.get());
        copyFlatUrl.addActionListener(new CopyUrlAction(LinkUtils.buildFlatThreadLink(messageId)));
        menu.add(copyFlatUrl);

        JMenuItem copyThreadUrl = new JMenuItem();
        copyThreadUrl.setText(Messages.Popup_View_ThreadsTree_CopyUrl_Thread.get());
        copyThreadUrl.addActionListener(new CopyUrlAction(LinkUtils.buildThreadLink(messageId)));
        menu.add(copyThreadUrl);
        return menu;
    }

    static JMenuItem openMessage(int messageId, IAppControl appControl) {
        JMenuItem open = new JMenuItem();
        open.setText(Messages.Popup_View_ThreadsTree_OpenMessage.get());
        open.addActionListener(new OpenMessageAction(appControl, messageId, OpenMessageMethod.Default));
        return open;
    }

    static JMenuItem openMessageInTab(int messageId, IAppControl appControl) {
        JMenuItem open = new JMenuItem();
        open.setText(Messages.Popup_View_ThreadsTree_OpenMessage_NewTab.get());
        open.addActionListener(new OpenMessageAction(appControl, messageId, OpenMessageMethod.NewTab));
        return open;
    }

    /**
     * Adds to an menu a 'open in browser' action if it supported by current OS.
     *
     * @param menu        target menu to be filled with the menu item.
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
        JMenuItem copyToClipboard = new JMenuItem(Messages.Popup_Link_Copy_ToClipboard.get());
        copyToClipboard.addActionListener(new CopyUrlAction(url));

        return copyToClipboard;
    }

    static JMenuItem markReadUnreadSubmenu(ITreeItem<?> message, IAppControl appControl) {
        JMenu menu = new JMenu(Messages.Popup_View_ThreadsTree_Mark_Title.get());

        menu.add(new SetThreadReadMenuItem(Messages.Popup_View_ThreadsTree_Mark_ThreadRead, message, true));
        menu.add(new SetThreadReadMenuItem(Messages.Popup_View_ThreadsTree_Mark_ThreadUnread, message, false));

        menu.addSeparator();

//        menu.add(new ExtendedMarkRead(Messages.Popup_View_ThreadsTree_Mark_Extended, message, appControl));

        menu.addSeparator();

        menu.add(new SetForumReadMenuItem(Messages.Popup_View_Forums_SetReadAll, message.getForumId(), true));
        menu.add(new SetForumReadMenuItem(Messages.Popup_View_Forums_SetUnreadAll, message.getForumId(), false));

        return menu;
    }

    /**
     * Creates a submenu to add the
     * @param post
     * @param appControl
     * @return
     */
    public static JMenuItem favoritesSubmenu(Post post, IAppControl appControl) {
        JMenu menu = new JMenu("Add to favorite");

        int topicId = post.getTopicId();
        if (topicId == 0) {
            // The post is a thread.
            topicId = post.getMessageId();
        }
        menu.add(new AddToFavoriteMenuItem("Thread", FavoriteType.UnreadPostsInThread, topicId));
//        menu.add(new AddToFavoriteMenuItem("Sub-thread", FavoriteType.UnreadPostResponses, post.getMessageId()));
        menu.addSeparator();
        menu.add(new AddToFavoriteMenuItem(post.getMessageData().getUserName() + "'s posts", FavoriteType.UnreadUserPosts, post.getMessageData().getUserId()));
        menu.add(new AddToFavoriteMenuItem("Responses on " + post.getMessageData().getUserName() + "'s posts", FavoriteType.UnreadUserResponses, post.getMessageData().getUserId()));
//        menu.addSeparator();
//        menu.add(new AddToFavoriteMenuItem("Add thread to favorites", FavoriteType.Category, post.getMessageData().getCategory()));

        return menu;
    }
}
