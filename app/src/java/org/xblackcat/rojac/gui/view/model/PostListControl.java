package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang.StringUtils;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.IUserAH;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;
import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author xBlackCat
 */
class PostListControl extends MessageListControl {
    private final boolean replies;

    /**
     * Creates a post list control. All posts a linked with the specified user. If parameter is <code>true</code> - the
     * control loads all replies on the user posts. If parameter is <code>false</code> - the control loads all posts of
     * the user.
     *
     * @param replies set true to load replies of the user instead of user posts.
     */
    public PostListControl(boolean replies) {
        this.replies = replies;
    }

    public void fillModelByItemId(final AThreadModel<Post> model, final int itemId) {
        final PostList root = new PostList(itemId);
        model.setRoot(root);

        new RojacWorker<Void, String>() {
            @Override
            protected Void perform() throws Exception {
                IUserAH userAH = ServiceFactory.getInstance().getStorage().getUserAH();

                User userById = userAH.getUserById(itemId);
                if (userById != null) {
                    publish(userById.getUserNick());
                }

                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String userName : chunks) {
                    if (userName != null) {
                        root.setMessageData(
                                new MessageData(-1, -1, -1, -1, itemId, "", userName, -1, -1, true, null, false)
                        );
                    }
                }

                model.nodeChanged(root);
            }
        }.execute();

        new PostListLoader(model).execute();
    }

    @Override
    public JPopupMenu getTitlePopup(AThreadModel<Post> model, IAppControl appControl) {
        Post root = model.getRoot();

        return replies ?
                PopupMenuBuilder.getReplyListTabMenu(root, appControl) :
                PopupMenuBuilder.getPostListTabMenu(root, appControl);
    }

    @Override
    public Icon getTitleIcon(AThreadModel<Post> model) {
        Post root = model.getRoot();
        if (root == null) {
            return null;
        }

        ReadStatus readStatus = root.isRead();

        ReadStatusIcon statusIcon = replies ? ReadStatusIcon.ReplyList : ReadStatusIcon.PostList;

        return UIUtils.getIcon(statusIcon.getIcon(readStatus));
    }

    protected void updateModel(final AThreadModel<Post> model, Runnable postProcessor) {
        assert RojacUtils.checkThread(true);

        new PostListLoader(postProcessor, model).execute();
    }

    @Override
    public String getTitle(AThreadModel<Post> model) {
        Post root = model.getRoot();

        if (root == null) {
            return "#";
        }

        String userName = root.getMessageData().getUserName();
        if (StringUtils.isBlank(userName)) {
            userName = "#" + root.getMessageData().getUserId();
        }

        Message textBase = replies ? Message.Favorite_UserReplies_Name : Message.Favorite_UserPosts_Name;
        return textBase.get(userName);
    }

    private class PostListLoader extends RojacWorker<Void, Void> {
        private Iterable<MessageData> messages;
        private final int itemId;
        private final AThreadModel<Post> model;

        public PostListLoader(Runnable postProcessor, AThreadModel<Post> model) {
            super(postProcessor);
            this.itemId = model.getRoot().getMessageData().getUserId();
            this.model = model;
        }

        public PostListLoader(AThreadModel<Post> model) {
            this(null, model);
        }

        @Override
        protected Void perform() throws Exception {
            IStorage storage = ServiceFactory.getInstance().getStorage();
            if (replies) {
                messages = storage.getMessageAH().getUserReplies(itemId);
            } else {
                messages = storage.getMessageAH().getUserPosts(itemId);
            }
            return null;
        }

        @Override
        protected void done() {
            PostList root = (PostList) model.getRoot();

            root.fillList(messages);
            root.setLoadingState(LoadingState.Loaded);
            model.markInitialized();
            model.nodeStructureChanged(root);
            model.fireResortModel();
        }
    }
}
