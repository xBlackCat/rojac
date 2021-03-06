package org.xblackcat.rojac.gui.popup;

import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.NewMessagesUpdatedPacket;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.INewMessageAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.LinkUtils;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.util.List;

/**
 * Class for prepare pop-up menus for the application.
 *
 * @author xBlackCat
 */
public final class PopupMenuBuilder {
    private PopupMenuBuilder() {
    }

    /**
     * Constructs a pop-up menu for link in message pane.
     *
     * @param messageId
     * @param appControl back link to main window control.
     * @param postFound
     * @return new popup menu for the specified url.
     */
    public static JPopupMenu getPostMenu(int messageId, IAppControl appControl, boolean postFound) {
        // Build menu
        final JPopupMenu menu = new JPopupMenu();

        String headerText = "#" + messageId;
        JMenuItem headerItem = new JMenuItem(headerText);
        headerItem.setEnabled(false);
        menu.add(headerItem);

        if (postFound) {
            menu.addSeparator();
            menu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInForum, OpenMessageMethod.InForum));
            menu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInTab, OpenMessageMethod.NewTab));
            menu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInThread, OpenMessageMethod.InThread));
        }

        menu.addSeparator();
        MenuHelper.addOpenLink(menu, Message.Popup_Link_Open_InBrowser_Message, LinkUtils.buildMessageLink(messageId));
        MenuHelper.addOpenLink(menu, Message.Popup_Link_Open_InBrowser_Thread, LinkUtils.buildThreadLink(messageId));

        menu.add(MenuHelper.copyToClipboard(LinkUtils.buildMessageLink(messageId)));
        menu.add(MenuHelper.copyLinkSubmenu(messageId));

        return menu;
    }

    public static JPopupMenu getForumViewMenu(Forum forum, IAppControl appControl) {
        JPopupMenu menu = new JPopupMenu(forum.getForumName());

        final boolean subscribed = forum.isSubscribed();
        final int forumId = forum.getForumId();

        menu.add(new OpenForumAction(appControl, forumId));
        menu.addSeparator();

        menu.add(new SetForumReadMenuItem(Message.Popup_View_SetReadAll, forumId, true));
        menu.add(new SetForumReadMenuItem(Message.Popup_View_SetUnreadAll, forumId, false));

        menu.add(new ExtendedMarkRead(Message.Popup_View_ThreadsTree_Mark_Extended, forum, appControl.getMainFrame()));

        menu.addSeparator();

        JCheckBoxMenuItem mi = new JCheckBoxMenuItem(Message.Popup_View_Forums_Subscribe.get(), subscribed);
        mi.addActionListener(new SubscribeChangeListener(forumId, subscribed));
        menu.add(mi);


        return menu;
    }

    public static JPopupMenu getTreeViewMenu(Post post, IAppControl appControl, boolean addFavorites) {
        int messageId = post.getMessageId();
        int userId = post.getMessageData().getUserId();

        final JPopupMenu menu = new JPopupMenu("#" + messageId);

        JMenuItem item = new JMenuItem("#" + messageId);
        item.setEnabled(false);

        menu.add(item);

        JMenuItem openSubMenu = menu.add(new JMenu(Message.Popup_Open_SubMenu.get()));
        openSubMenu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInTab, OpenMessageMethod.NewTab));
        openSubMenu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInThread, OpenMessageMethod.InThread));
        openSubMenu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInForum, OpenMessageMethod.InForum));
        if (userId > 0) {
            // Menu items for non-anonymous
            String userName = post.getMessageData().getUserName();
            openSubMenu.add(new OpenPostList(userId, appControl, Message.Popup_Open_UserPostList.get(userName), ViewType.PostList));
            openSubMenu.add(new OpenPostList(userId, appControl, Message.Popup_Open_UserReplyList.get(userName), ViewType.ReplyList));
        }

        menu.addSeparator();

        if (post.getThreadRoot() != null) {
            menu.add(new IgnoreTopicToggleMenuItem(post.getThreadRoot().getMessageData()));
        }
        menu.add(new IgnoreUserToggleMenuItem(post.getMessageData()));

        menu.addSeparator();

        menu.add(new SetMessageReadMenuItem(Message.Popup_View_ThreadsTree_Mark_Read, post.getMessageData(), true));
        menu.add(new SetMessageReadMenuItem(Message.Popup_View_ThreadsTree_Mark_Unread, post.getMessageData(), false));

        menu.add(MenuHelper.markReadUnreadSubmenu(post, appControl.getMainFrame()));

        menu.addSeparator();
        MenuHelper.addOpenLink(menu, Message.Popup_Link_Open_InBrowser_Message, LinkUtils.buildMessageLink(messageId));
        MenuHelper.addOpenLink(menu, Message.Popup_Link_Open_InBrowser_Thread, LinkUtils.buildThreadLink(messageId));
        menu.add(MenuHelper.copyLinkSubmenu(messageId));

        if (addFavorites) {
            menu.addSeparator();
            menu.add(MenuHelper.favoritesSubmenu(post.getMessageData(), appControl));
        }

        return menu;
    }

    public static JPopupMenu getFavoritesMenu(Favorite favorite, IAppControl appControl) {
        final JPopupMenu menu = new JPopupMenu(favorite.getName());

        final int favoriteId = favorite.getId();
        menu.add(new OpenFavoriteAction(appControl, favoriteId));
        menu.addSeparator();

        menu.add(new RemoveFavoriteAction(favoriteId));

        return menu;
    }

    public static JPopupMenu getForumViewTabMenu(Forum forum, IAppControl appControl) {
        JPopupMenu menu = new JPopupMenu(forum.getForumName());

        final boolean subscribed = forum.isSubscribed();
        final int forumId = forum.getForumId();

        menu.add(new SetForumReadMenuItem(Message.Popup_View_SetReadAll, forumId, true));
        menu.add(new SetForumReadMenuItem(Message.Popup_View_SetUnreadAll, forumId, false));

        menu.add(new ExtendedMarkRead(Message.Popup_View_ThreadsTree_Mark_Extended, forum, appControl.getMainFrame()));

        menu.addSeparator();

        JCheckBoxMenuItem mi = new JCheckBoxMenuItem(Message.Popup_View_Forums_Subscribe.get(), subscribed);
        mi.addActionListener(new SubscribeChangeListener(forumId, subscribed));
        menu.add(mi);

        return menu;
    }

    public static JPopupMenu getThreadViewTabMenu(Post post, IAppControl appControl, boolean addToFavorites) {
        int messageId = post.getMessageId();
        JPopupMenu menu = new JPopupMenu("#" + messageId);

        menu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInForum, OpenMessageMethod.InForum));

        menu.addSeparator();

        menu.add(new SetThreadReadMenuItem(Message.Popup_View_SetReadAll, post, true));
        menu.add(new SetThreadReadMenuItem(Message.Popup_View_SetUnreadAll, post, false));

        menu.add(new ExtendedMarkRead(Message.Popup_View_ThreadsTree_Mark_Extended, post.getMessageData(), appControl.getMainFrame()));

        menu.addSeparator();
        MenuHelper.addOpenLink(menu, Message.Popup_Link_Open_InBrowser_Thread, LinkUtils.buildThreadLink(messageId));
        menu.add(MenuHelper.copyLinkSubmenu(messageId));

        if (addToFavorites) {
            menu.addSeparator();
            String text = Message.Popup_Favorites_Add.get(Message.Popup_Favorites_Add_Thread.get().toLowerCase(Property.ROJAC_GUI_LOCALE.get()));
            menu.add(new AddToFavoriteMenuItem(text, FavoriteType.Thread, post.getMessageData().getThreadRootId()));
        }

        return menu;
    }

    public static JPopupMenu getMessageViewTabMenu(MessageData message, IAppControl appControl) {
        int messageId = message.getMessageId();
        final JPopupMenu menu = new JPopupMenu("#" + messageId);

        JMenuItem item = new JMenuItem("#" + messageId);
        item.setEnabled(false);

        menu.add(item);

        menu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInThread, OpenMessageMethod.InThread));
        menu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInForum, OpenMessageMethod.InForum));

        menu.addSeparator();
        MenuHelper.addOpenLink(menu, Message.Popup_Link_Open_InBrowser_Message, LinkUtils.buildMessageLink(messageId));
        MenuHelper.addOpenLink(menu, Message.Popup_Link_Open_InBrowser_Thread, LinkUtils.buildThreadLink(messageId));
        menu.add(MenuHelper.copyLinkSubmenu(messageId));

        return menu;
    }

    public static JPopupMenu getRecentPostsMenu(MessageData threadRoot, IAppControl appControl) {
        int messageId = threadRoot.getMessageId();
        final JPopupMenu menu = new JPopupMenu("#" + messageId);

        JMenuItem item = new JMenuItem("#" + messageId);
        item.setEnabled(false);

        menu.add(item);

        menu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInThread, OpenMessageMethod.InThread));
        menu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInForum, OpenMessageMethod.InForum));

        menu.addSeparator();

        menu.add(new IgnoreTopicToggleMenuItem(threadRoot));

        menu.addSeparator();

        menu.add(new SetThreadReadMenuItem(Message.Popup_View_SetReadAll, threadRoot, true));
        menu.add(new SetThreadReadMenuItem(Message.Popup_View_SetUnreadAll, threadRoot, false));

        menu.addSeparator();
        MenuHelper.addOpenLink(menu, Message.Popup_Link_Open_InBrowser_Thread, LinkUtils.buildThreadLink(messageId));
        menu.add(MenuHelper.copyLinkSubmenu(messageId));

        menu.addSeparator();
        String text = Message.Popup_Favorites_Add.get(Message.Popup_Favorites_Add_Thread.get().toLowerCase(Property.ROJAC_GUI_LOCALE.get()));
        menu.add(new AddToFavoriteMenuItem(text, FavoriteType.Thread, threadRoot.getThreadRootId()));

        return menu;
    }

    public static JPopupMenu getFavoriteMessagesListTabMenu(Post post, IAppControl appControl) {
        return getMessagesListTabMenu(post, appControl, null, null);
    }

    public static JPopupMenu getPostListTabMenu(Post post, IAppControl appControl) {
        return getMessagesListTabMenu(post, appControl, FavoriteType.UserPosts, Message.Popup_Favorites_Add_UserPosts.get(post.getMessageData().getUserName()));
    }

    public static JPopupMenu getReplyListTabMenu(Post post, IAppControl appControl) {
        return getMessagesListTabMenu(post, appControl, FavoriteType.UserResponses, Message.Popup_Favorites_Add_ToUserReplies.get(post.getMessageData().getUserName()));
    }

    public static JPopupMenu getNewMessageListMenu(final Post post, final IAppControl appControl) {
        JPopupMenu menu = new JPopupMenu("#" + (-post.getMessageId()));

        JMenuItem edit = new JMenuItem(Message.Popup_View_OutboxTree_Edit.get());
        menu.add(new SendToggleMenuItem(post.getMessageData()));

        menu.addSeparator();
        edit.addActionListener(e -> appControl.editMessage(null, -post.getMessageId()));
        menu.add(edit);
        JMenuItem remove = new JMenuItem(Message.Popup_View_OutboxTree_Remove.get());

        remove.addActionListener(
                e -> new RojacWorker<Void, Void>() {
                    @Override
                    protected Void perform() throws Exception {
                        Storage.get(INewMessageAH.class).removeNewMessage(-post.getMessageId());

                        publish();
                        return null;
                    }

                    @Override
                    protected void process(List<Void> chunks) {
                        new NewMessagesUpdatedPacket().dispatch();
                    }
                }.execute()
        );
        menu.add(remove);
        menu.add(new JSeparator());
        JMenuItem removeAll = new JMenuItem(Message.Popup_View_OutboxTree_RemoveAll.get());
        removeAll.addActionListener(
                e -> new RojacWorker<Void, Void>() {
                    @Override
                    protected Void perform() throws Exception {
                        Storage.get(INewMessageAH.class).purgeNewMessage();

                        publish();
                        return null;
                    }

                    @Override
                    protected void process(List<Void> chunks) {
                        new NewMessagesUpdatedPacket().dispatch();
                    }
                }.execute()
        );

        menu.add(removeAll);
        return menu;
    }

    public static JPopupMenu getIgnoredListMenu(Post post, IAppControl appControl) {
        int messageId = post.getMessageId();

        final JPopupMenu menu = new JPopupMenu("#" + messageId);
        JMenuItem item = new JMenuItem("#" + messageId);
        item.setEnabled(false);

        menu.add(item);
        menu.add(new IgnoreTopicToggleMenuItem(post.getMessageData()));

        menu.addSeparator();

        JMenuItem openSubMenu = menu.add(new JMenu(Message.Popup_Open_SubMenu.get()));
        openSubMenu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInTab, OpenMessageMethod.NewTab));
        openSubMenu.add(new OpenMessageMenuItem(messageId, appControl, Message.Popup_Open_MessageInThread, OpenMessageMethod.InThread));

        menu.addSeparator();
        MenuHelper.addOpenLink(menu, Message.Popup_Link_Open_InBrowser_Message, LinkUtils.buildMessageLink(messageId));
        MenuHelper.addOpenLink(menu, Message.Popup_Link_Open_InBrowser_Thread, LinkUtils.buildThreadLink(messageId));
        menu.add(MenuHelper.copyLinkSubmenu(messageId));

        return menu;
    }

    private static JPopupMenu getMessagesListTabMenu(Post post, IAppControl appControl, FavoriteType favoriteType, String info) {
        JPopupMenu menu = new JPopupMenu("#");

        menu.add(new SetMessageListReadMenuItem(Message.Popup_View_SetReadAll, post, true));
        menu.add(new SetMessageListReadMenuItem(Message.Popup_View_SetUnreadAll, post, false));

        menu.add(new ExtendedMarkRead(Message.Popup_View_ThreadsTree_Mark_Extended, appControl.getMainFrame()));

        if (favoriteType != null && info != null) {
            menu.addSeparator();
            String text = Message.Popup_Favorites_Add.get(info.toLowerCase(Property.ROJAC_GUI_LOCALE.get()));
            menu.add(new AddToFavoriteMenuItem(text, favoriteType, post.getMessageData().getUserId()));
        }
        return menu;
    }
}
