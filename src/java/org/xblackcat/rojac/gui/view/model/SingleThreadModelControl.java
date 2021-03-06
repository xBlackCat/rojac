package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.NotImplementedException;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.thread.ThreadToolbarActions;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;
import java.util.Collections;

/**
 * Control class of all threads of the specified forum.
 *
 * @author xBlackCat
 */

class SingleThreadModelControl extends AThreadsModelControl {
    private static final ThreadToolbarActions[] TOOLBAR_CONFIG = new ThreadToolbarActions[]{
            ThreadToolbarActions.ToThreadRoot,
            ThreadToolbarActions.PreviousPost,
            ThreadToolbarActions.NextPost,
            ThreadToolbarActions.PreviousUnread,
            ThreadToolbarActions.NextUnread,
            null,
            ThreadToolbarActions.MarkSubTreeRead,
            ThreadToolbarActions.MarkThreadRead
    };

    @Override
    public void fillModelByItemId(final SortedThreadsModel model, int threadId) {
        assert RojacUtils.checkThread(true);

        MessageData fakeMessageData = new MessageData(threadId, 0, 0, 0, 0, null, "Loading...", 0L, 0L, false, null, false, 0, false);
        final Thread rootItem = new Thread(fakeMessageData, null);

        model.setRoot(rootItem);

        new MessageChecker(threadId) {
            @Override
            protected void done() {
                if (data != null) {
                    rootItem.setMessageData(data);
                    model.nodeChanged(rootItem);

                    reloadThread(model, postProcessor);
                } else {
                    model.setRoot(null);
                }
            }
        }.execute();
    }


    @Override
    public boolean isRootVisible() {
        assert RojacUtils.checkThread(true);

        return true;
    }

    @Override
    public String getTitle(SortedThreadsModel model) {
        assert RojacUtils.checkThread(true);

        // Root is Thread object
        Post root = model.getRoot();
        if (root != null) {
            if (root.getMessageData() != null) {
                if (root.getMessageData().getSubject() != null) {
                    return root.getMessageData().getSubject();
                }
            }
            return "#" + root.getMessageId();
        }

        return "#";
    }

    @Override
    public void processPacket(final SortedThreadsModel model, IPacket p, final Runnable postProcessor) {
        final int forumId = model.getRoot().getForumId();
        final int threadId = model.getRoot().getMessageId();

        new PacketDispatcher(
                new APacketProcessor<OptionsUpdatedPacket>() {
                    @Override
                    public void process(OptionsUpdatedPacket p) {
                    if (p.isPropertyAffected(Property.SKIP_IGNORED_USER_REPLY) ||
                            p.isPropertyAffected(Property.SKIP_IGNORED_USER_THREAD)) {
                        model.subTreeNodesChanged(model.getRoot());
                    }
                    }
                },
                new APacketProcessor<SetForumReadPacket>() {
                    @Override
                    public void process(SetForumReadPacket p) {
                    if (p.getForumId() == forumId) {
                        assert RojacUtils.checkThread(true);

                        // Root post is Thread object
                        model.getRoot().setDeepRead(p.isRead());

                        model.subTreeNodesChanged(model.getRoot());
                    }
                    }
                },
                new APacketProcessor<SetSubThreadReadPacket>() {
                    @Override
                    public void process(SetSubThreadReadPacket p) {
                    if (p.getForumId() == forumId) {
                        assert RojacUtils.checkThread(true);

                        // Root post is Thread object
                        Post root = model.getRoot().getMessageById(p.getPostId());
                        if (root == null) {
                            return;
                        }

                        root.setDeepRead(p.isRead());

                        model.subTreeNodesChanged(root);
                    }
                    }
                },
                new APacketProcessor<SetPostReadPacket>() {
                    @Override
                    public void process(SetPostReadPacket p) {
                    MessageData md = p.getPost();
                    if (md.getForumId() == forumId && md.getThreadRootId() == threadId) {
                        assert RojacUtils.checkThread(true);

                        final Post post = model.getRoot().getMessageById(md.getMessageId());
                        if (post != null) {
                            post.setRead(p.isRead());
                            model.pathToNodeChanged(post);
                        }
                    }
                    }
                },
                new APacketProcessor<SetReadExPacket>() {
                    @Override
                    public void process(SetReadExPacket p) {
                    if (!p.isTopicAffected(threadId)) {
                        // Current forum is not changed - have a rest
                        return;
                    }

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
                new APacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                    if (!p.isForumAffected(forumId)) {
                        // Current forum is not changed - have a rest
                        return;
                    }
                    if (!p.isTopicAffected(threadId)) {
                        return;
                    }

                    reloadThread(model, postProcessor);
                    }
                },
                new APacketProcessor<IgnoreUserUpdatedPacket>() {
                    @Override
                    public void process(IgnoreUserUpdatedPacket p) {
                        PostUtils.setIgnoreUserFlag(model, p.getUserId(), p.isIgnored());
                    }
                },
                new APacketProcessor<IgnoreUpdatedPacket>() {
                    @Override
                    public void process(IgnoreUpdatedPacket p) {
                    if (p.getThreadId() == threadId) {
                        Post threadRoot = model.getRoot();
                        MessageData data = threadRoot.getMessageData();
                        threadRoot.setMessageData(data.setIgnored(p.isIgnored()));

                        model.subTreeNodesChanged(threadRoot);
                    }
                }
    }
        ).dispatch(p);
    }

    private void reloadThread(final SortedThreadsModel model, final Runnable postProcessor) {
        final Thread root = (Thread) model.getRoot();

        root.setLoadingState(LoadingState.Loading);

        // Thread always filled in.
        new ThreadPostsLoader(
                model, root, new Runnable() {
            @Override
            public void run() {
                model.markInitialized();
                model.fireResortModel();

                if (postProcessor != null) {
                    postProcessor.run();
                }
            }
        }
        ).execute();
    }

    @Override
    public Icon getTitleIcon(SortedThreadsModel model) {
        Icon threadIcon = null;

        Post root = model.getRoot();

        if (root != null) {
            int userId = Property.RSDN_USER_ID.get();
            if (userId > 0 && hasUnreadReplies(userId, root)) {
                threadIcon = ReadStatusIcon.ThreadHasResponse.getIcon(ReadStatus.Unread);
            } else {
                threadIcon = ReadStatusIcon.Thread.getIcon(root.isRead());
            }
        }

        return threadIcon;
    }

    @Override
    public JPopupMenu getTitlePopup(SortedThreadsModel model, IAppControl appControl) {
        Post root = model.getRoot();

        if (root != null) {
            return PopupMenuBuilder.getThreadViewTabMenu(root, appControl, true);
        }

        return null;
    }


    @Override
    public ThreadToolbarActions[] getToolbar() {
        return TOOLBAR_CONFIG;
    }

    @Override
    public boolean isToolBarButtonVisible(ThreadToolbarActions action, Post post) {
        switch (action) {
            case ToThreadRoot:
                return post != null;
            case MarkSubTreeRead:
                return post != null;
            case MarkThreadRead:
                return post != null;
            case PreviousPost:
            case NextPost:
            case PreviousUnread:
            case NewThread:
            case NextUnread:
                return true;
            default:
                return false;
        }
    }

    private boolean hasUnreadReplies(int userId, Post post) {
        boolean ownPost = post.getMessageData().getUserId() == userId;
        for (Post p : post.childrenPosts) {
            if (ownPost && !p.messageData.isRead()) {
                return true;
            } else if (hasUnreadReplies(userId, p)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void unloadThread(SortedThreadsModel model, Post item) {
        // Single-thread views shouldn't clean the thread info.
    }

    @Override
    public void loadHiddenItems(SortedThreadsModel model, int itemId) {
        throw new NotImplementedException("The method shouldn't be used.");
    }

    @Override
    public Post addPost(SortedThreadsModel model, Post root, MessageData data) {
        Post p = new Post(data, root);
        root.childrenPosts.add(p);

        Collections.sort(root.childrenPosts);

        model.nodeAdded(root, p);

        return p;

    }

    @Override
    public Property<OpenMessageMethod> getOpenMessageMethod() {
        return Property.OPEN_MESSAGE_BEHAVIOUR_TOPIC_VIEW;
    }
}
