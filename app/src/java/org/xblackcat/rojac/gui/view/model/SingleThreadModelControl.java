package org.xblackcat.rojac.gui.view.model;

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
    public void fillModelByItemId(final AThreadModel<Post> model, int threadId) {
        assert RojacUtils.checkThread(true);

        MessageData fakeMessageData = new MessageData(threadId, 0, 0, 0, 0, null, "Loading...", 0L, 0L, false, null, false, 0);
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
    public String getTitle(AThreadModel<Post> model) {
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
    public void processPacket(final AThreadModel<Post> model, IPacket p, final Runnable postProcessor) {
        final int forumId = model.getRoot().getForumId();
        final int threadId = model.getRoot().getMessageId();

        new PacketDispatcher(
                new IPacketProcessor<SetForumReadPacket>() {
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
                new IPacketProcessor<SetSubThreadReadPacket>() {
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
                new IPacketProcessor<SetPostReadPacket>() {
                    @Override
                    public void process(SetPostReadPacket p) {
                        MessageData post = p.getPost();
                        if (post.getForumId() == forumId && post.getThreadRootId() == threadId) {
                            markPostRead(model, post.getMessageId(), p.isRead());
                        }
                    }
                },
                new IPacketProcessor<SetReadExPacket>() {
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
                new IPacketProcessor<SynchronizationCompletePacket>() {
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
                new IPacketProcessor<IgnoreUpdatedPacket>() {
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

    private void reloadThread(final AThreadModel<Post> model, final Runnable postProcessor) {
        final Thread root = (Thread) model.getRoot();

        root.setLoadingState(LoadingState.Loading);

        // Thread always filled in.
        new ThreadLoader(model, root, new Runnable() {
            @Override
            public void run() {
                model.markInitialized();
                model.fireResortModel();

                if (postProcessor != null) {
                    postProcessor.run();
                }
            }
        }).execute();
    }

    @Override
    public Icon getTitleIcon(AThreadModel<Post> model) {
        Icon threadIcon = null;

        Post root = model.getRoot();

        if (root != null) {
            int userId = Property.RSDN_USER_ID.get(0);
            if (userId > 0 && hasUnreadReplies(userId, root)) {
                threadIcon = ReadStatusIcon.ThreadHasResponse.getIcon(ReadStatus.Unread);
            } else {
                threadIcon = ReadStatusIcon.Thread.getIcon(root.isRead());
            }
        }

        return threadIcon;
    }

    @Override
    public JPopupMenu getTitlePopup(AThreadModel<Post> model, IAppControl appControl) {
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
    public void unloadThread(AThreadModel<Post> model, Post item) {
        // Single-thread views shouldn't clean the thread info.
    }

    @Override
    public Property<OpenMessageMethod> getOpenMessageMethod() {
        return Property.OPEN_MESSAGE_BEHAVIOUR_TOPIC_VIEW;
    }
}
