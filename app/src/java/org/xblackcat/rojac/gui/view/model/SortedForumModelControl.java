package org.xblackcat.rojac.gui.view.model;

import gnu.trove.TIntHashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.data.ForumMessageData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.view.forumlist.ForumData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;

/**
 * Control class of all threads of the specified forum.
 *
 * @author xBlackCat
 */

public class SortedForumModelControl extends AThreadsModelControl {
    private static final Log log = LogFactory.getLog(SortedForumModelControl.class);

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    @Override
    public void fillModelByItemId(final AThreadModel<Post> model, final int forumId) {
        assert RojacUtils.checkThread(true);

        final ForumRoot rootItem = new ForumRoot(forumId);

        model.setRoot(rootItem);

        final ForumInfoLoader infoLoader = new ForumInfoLoader(model, forumId) {
            @Override
            protected void done() {
                super.done();
                if (model.getRoot() != null) {
                    new ThreadsLoader(model, forumId).execute();
                }
            }
        };
        infoLoader.execute();
    }

    private void markForumRead(AThreadModel<Post> model, boolean read) {
        assert RojacUtils.checkThread(true);

        // Root post is ForumRoot object
        model.getRoot().setRead(read);

        model.subTreeNodesChanged(model.getRoot());
    }

    @Override
    public boolean isRootVisible() {
        assert RojacUtils.checkThread(true);

        return false;
    }

    private void updateModel(final AThreadModel<Post> model, int... threadIds) {
        assert RojacUtils.checkThread(true);

        TIntHashSet filledThreads = new TIntHashSet();

        Post root = model.getRoot();
        // Should be ForumRoot object
        assert root instanceof ForumRoot;

        for (Post post : root.childrenPosts) {
            assert post instanceof Thread;

            if (post.getThreadRoot().isFilled()) {
                filledThreads.add(post.getThreadRoot().getMessageId());
            }
        }

        // Retain only changed filled thread to reload.
        filledThreads.retainAll(threadIds);

        // Reload filled threads.
        for (int threadId : filledThreads.toArray()) {
            Post post = root.getMessageById(threadId);
            if (post != null) {
                // Update thread children
                final Thread t = post.getThreadRoot();

                if (t.isFilled()) {
                    // Thread is already filled - update children
                    loadThread(model, t, new Runnable() {
                        @Override
                        public void run() {
                            model.nodeStructureChanged(t);
                        }
                    });
                } else {
                    throw new RojacDebugException("Expected filled thread #" + threadId);
                }
            } else {
                throw new RojacDebugException("Thread #" + threadId + " not found");
            }
        }

        // Reload forum threads list.
        new ThreadsLoader(model, root.getForumId()).execute();
    }

    @Override
    public String getTitle(AThreadModel<Post> model) {
        // Root is ForumRoot object
        Post root = model.getRoot();
        if (root != null) {
            if (root.getMessageData() != null) {
                if (root.getMessageData().getSubject() != null) {
                    return root.getMessageData().getSubject();
                }
            }
            return "#" + root.getForumId();
        }

        return "#";
    }

    @Override
    public void processPacket(final AThreadModel<Post> model, IPacket p, Runnable postProcessor) {
        final int forumId = model.getRoot().getForumId();

        new PacketDispatcher(
                new IPacketProcessor<SetForumReadPacket>() {
                    @Override
                    public void process(SetForumReadPacket p) {
                        if (p.getForumId() == forumId) {
                            markForumRead(model, p.isRead());
                        }
                    }
                },
                new IPacketProcessor<SetPostReadPacket>() {
                    @Override
                    public void process(SetPostReadPacket p) {
                        if (p.getForumId() == forumId) {
                            if (p.isRecursive()) {
                                // Post is a root of marked thread
                                markThreadRead(model, p.getPostId(), p.isRead());
                            } else {
                                // Mark as read only the post
                                markPostRead(model, p.getPostId(), p.isRead());
                            }
                        }
                    }
                },
                new IPacketProcessor<SetReadExPacket>() {
                    @Override
                    public void process(SetReadExPacket p) {
                        if (!p.isForumAffected(forumId)) {
                            // Current forum is not changed - have a rest
                            return;
                        }

                        boolean newReadState = p.isRead();
                        Post root = model.getRoot();

                        // First, queue for update a not loaded threads.
                        for (int topicId : p.getThreadIds()) {
                            Post post = root.getMessageById(topicId);

                            if (post == null) {
                                // Topic from another forum - skip.
                                continue;
                            }

                            assert post instanceof Thread : post;
                            assert post.getThreadRoot() == post : post;
                            Thread topic = (Thread) post;

                            if (!topic.isFilled()) {
                                // Queue updatestat data.
                                new ThreadUnreadPostsLoader(topic, model).execute();
                            }
                        }

                        // Second - update already loaded posts.
                        for (int postId : p.getMessageIds()) {
                            Post post = root.getMessageById(postId);

                            if (post != null) {
                                post.setRead(newReadState);
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

                        updateModel(model, p.getThreadIds());
                    }
                },
                new IPacketProcessor<SubscriptionChangedPacket>() {
                    @Override
                    public void process(SubscriptionChangedPacket p) {
                        final Post root = model.getRoot();
                        final MessageData data = root.getMessageData();

                        if (data instanceof ForumMessageData) {
                            ForumData f = ((ForumMessageData) data).getForum();

                            for (SubscriptionChangedPacket.Subscription s : p.getNewSubscriptions()) {
                                if (s.getForumId() == f.getForumId()) {
                                    f.setSubscribed(s.isSubscribed());

                                    model.nodeChanged(root);
                                    return;
                                }
                            }
                        }
                    }
                }
        ).dispatch(p);
    }

    @Override
    public Icon getTitleIcon(AThreadModel<Post> model) {
        return null;
    }

    @Override
    public JPopupMenu getTitlePopup(AThreadModel<Post> model, IAppControl appControl) {
        final MessageData data = model.getRoot().getMessageData();

        if (data instanceof ForumMessageData) {
            ForumData f = ((ForumMessageData) data).getForum();

            return PopupMenuBuilder.getForumViewTabMenu(f, appControl);
        }

        return null;
    }

}
