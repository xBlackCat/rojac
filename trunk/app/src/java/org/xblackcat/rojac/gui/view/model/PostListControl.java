package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang3.StringUtils;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IUserAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;

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
                IUserAH userAH = Storage.get(IUserAH.class);

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
                                new MessageData(-1, -1, -1, -1, itemId, "", userName, -1, -1, true, null, false, 0, false)
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

        return statusIcon.getIcon(readStatus);
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

    @Override
    public void onDoubleClick(Post post, IAppControl appControl) {
        OpenMessageMethod openMethod = Property.OPEN_MESSAGE_BEHAVIOUR_POST_LIST.get();
        if (openMethod != null) {
            appControl.openMessage(post.getMessageId(), openMethod);
        }
    }

    @Override
    public JPopupMenu getItemMenu(Post post, IAppControl appControl) {
        return PopupMenuBuilder.getTreeViewMenu(post, appControl, false);
    }

    @Override
    public void processPacket(final AThreadModel<Post> model, IPacket p, final Runnable postProcessor) {
        new PacketDispatcher(
                new IPacketProcessor<OptionsUpdatedPacket>() {
                    @Override
                    public void process(OptionsUpdatedPacket p) {
                        if (p.isPropertyAffected(Property.SKIP_IGNORED_USER_REPLY) ||
                                p.isPropertyAffected(Property.SKIP_IGNORED_USER_THREAD)) {
                            model.subTreeNodesChanged(model.getRoot());
                        }
                    }
                },
                new IPacketProcessor<SetForumReadPacket>() {
                    @Override
                    public void process(SetForumReadPacket p) {
                        updateModel(model, postProcessor);
                    }
                },
                new IPacketProcessor<SetSubThreadReadPacket>() {
                    @Override
                    public void process(SetSubThreadReadPacket p) {
                        updateModel(model, postProcessor);
                    }
                },
                new IPacketProcessor<SetPostReadPacket>() {
                    @Override
                    public void process(SetPostReadPacket p) {
                        markPostRead(model, p.getPost().getMessageId(), p.isRead());
                    }
                },
                new IPacketProcessor<SetReadExPacket>() {
                    @Override
                    public void process(SetReadExPacket p) {
                        Post root = model.getRoot();

                        // Second - update already loaded posts.
                        for (int postId : p.getMessageIds()) {
                            Post post = root.getMessageById(postId);

                            if (post != null) {
                                post.setRead(p.isRead());
                                model.pathToNodeChanged(post);
                            }
                        }
                    }
                },
                new IPacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                        updateModel(model, postProcessor);
                    }
                }
        ).dispatch(p);
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
            if (replies) {
                messages = Storage.get(IMessageAH.class).getUserReplies(itemId);
            } else {
                messages = Storage.get(IMessageAH.class).getUserPosts(itemId);
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
