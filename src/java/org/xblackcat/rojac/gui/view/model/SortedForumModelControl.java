package org.xblackcat.rojac.gui.view.model;

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.set.hash.TIntHashSet;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumMessageData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.thread.ThreadToolbarActions;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Control class of all threads of the specified forum.
 *
 * @author xBlackCat
 */

class SortedForumModelControl extends AThreadsModelControl {
    private static final ThreadToolbarActions[] TOOLBAR_CONFIG = new ThreadToolbarActions[]{
            ThreadToolbarActions.NewThread,
            null,
            ThreadToolbarActions.ToThreadRoot,
            ThreadToolbarActions.PreviousPost,
            ThreadToolbarActions.NextPost,
            ThreadToolbarActions.PreviousUnread,
            ThreadToolbarActions.NextUnread,
            null,
            ThreadToolbarActions.ShowHidden,
            ThreadToolbarActions.MarkSubTreeRead,
            ThreadToolbarActions.MarkThreadRead,
            null,
            ThreadToolbarActions.IgnoreTopic,
            ThreadToolbarActions.FollowTopic,
            ThreadToolbarActions.IgnoreUser,
            ThreadToolbarActions.FollowUser,
            ThreadToolbarActions.IgnoreUnread
    };

    @Override
    public void fillModelByItemId(final SortedThreadsModel model, final int forumId) {
        assert RojacUtils.checkThread(true);

        final ForumRoot rootItem = new ForumRoot(forumId);

        model.setRoot(rootItem);

        new ForumInfoLoader(model, forumId).execute();
    }

    @Override
    public void loadHiddenItems(SortedThreadsModel model, int itemId) {
        new ThreadsLoader(null, model, model.getRoot().getForumId(), true).execute();
    }

    private void markForumRead(SortedThreadsModel model, boolean read) {
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

    private void updateModel(final Runnable postProcessor, final SortedThreadsModel model, int... threadIds) {
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
        final int[] filledThreadIds = filledThreads.toArray();

        for (int threadId : filledThreadIds) {
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
                            if (postProcessor != null) {
                                postProcessor.run();
                            }
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
        new ThreadsLoader(postProcessor, model, root.getForumId()).execute();
    }

    @Override
    public String getTitle(SortedThreadsModel model) {
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
    public void processPacket(final SortedThreadsModel model, IPacket p, final Runnable postProcessor) {
        final int forumId = model.getRoot().getForumId();

        new PacketDispatcher(
                new APacketProcessor<OptionsUpdatedPacket>() {
                    @Override
                    public void process(OptionsUpdatedPacket p) {
                        if (p.isPropertyAffected(Property.SKIP_IGNORED_USER_REPLY) ||
                                p.isPropertyAffected(Property.SKIP_IGNORED_USER_THREAD)) {
                        model.subTreeNodesChanged(model.getRoot());
                    }

                        if (p.isPropertyAffected(Property.VIEW_THREAD_HIDE_READ_THREADS) ||
                                p.isPropertyAffected(Property.HIDE_IGNORED_TOPICS)) {
                        if (Property.VIEW_THREAD_HIDE_READ_THREADS.get() || Property.HIDE_IGNORED_TOPICS.get()) {
                            hideReadThreads(model);
                        }

                        if (!Property.VIEW_THREAD_HIDE_READ_THREADS.get() || !Property.HIDE_IGNORED_TOPICS.get()) {
                            new ThreadsLoader(postProcessor, model, forumId).execute();
                        }
                    }
                    }
                },
                new APacketProcessor<SetForumReadPacket>() {
                    @Override
                    public void process(SetForumReadPacket p) {
                        if (p.getForumId() == forumId) {
                            markForumRead(model, p.isRead());
                    }
                    }
                },
                new APacketProcessor<SetSubThreadReadPacket>() {
                    @Override
                    public void process(SetSubThreadReadPacket p) {
                        if (p.getForumId() == forumId) {
                            markThreadRead(model, p.getPostId(), p.isRead());
                    }
                    }
                },
                new APacketProcessor<SetPostReadPacket>() {
                    @Override
                    public void process(SetPostReadPacket p) {
                        if (p.getPost().getForumId() == forumId) {
                        assert RojacUtils.checkThread(true);

                            final Post post = model.getRoot().getMessageById(p.getPost().getMessageId());
                        if (post != null) {
                                post.setRead(p.isRead());
                            model.pathToNodeChanged(post);
                        } else {
                                Post threadRoot = model.getRoot().getMessageById(p.getPost().getTopicId());

                            if (threadRoot != null) {
                                assert threadRoot instanceof Thread : "Expected a Thread class instance but got " + threadRoot.getClass().getName();
                                Thread thread = (Thread) threadRoot;
                                assert !thread.isFilled() : "Expecting not loaded thread";

                                // Update not-yet-loaded thread statistics
                                new ThreadStatisticLoader(thread, model, postProcessor).execute();
                            } else {
                                // Try to load hidden thread if any
                                new ThreadsLoader(postProcessor, model, forumId).execute();
                            }
                        }
                    }
                    }
                },
                new APacketProcessor<SetReadExPacket>() {
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
                            // Queue update stat data.
                            new ThreadStatisticLoader(topic, model, postProcessor).execute();
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
                new APacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                    if (Property.VIEW_THREAD_HIDE_READ_THREADS.get() || Property.HIDE_IGNORED_TOPICS.get()) {
                        hideReadThreads(model);
                    }

                        if (!p.isForumAffected(forumId)) {
                        // Current forum is not changed - have a rest
                        return;
                    }

                        updateModel(postProcessor, model, p.getThreadIds());
                    }
                },
                new APacketProcessor<SubscriptionChangedPacket>() {
                    @Override
                    public void process(SubscriptionChangedPacket p) {
                    final Post root = model.getRoot();
                    final MessageData data = root.getMessageData();

                    if (data instanceof ForumMessageData) {
                        Forum f = ((ForumMessageData) data).getForum();

                            for (SubscriptionChangedPacket.Subscription s : p.getNewSubscriptions()) {
                            if (s.getForumId() == f.getForumId()) {
                                f.setSubscribed(s.isSubscribed());

                                model.nodeChanged(root);
                                return;
                            }
                        }
                    }
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
                        if (p.getForumId() != forumId) {
                        return;
                    }

                        int threadId = p.getThreadId();

                    if (Property.HIDE_IGNORED_TOPICS.get()) {
                            if (p.isIgnored()) {
                            ForumRoot root = (ForumRoot) model.getRoot();

                            Post thread = root.getMessageById(threadId);
                            if (thread == null) {
                                // No thread
                                assert false : "Ignore non-existed thread #" + threadId + " in forum #" + forumId;
                                return;
                            }

                            int idx = root.removeThread(threadId);
                            if (idx == -1) {
                                // No thread (Again?!)
                                assert false : "Ignore non-existed thread #" + threadId + " in forum #" + forumId;
                                return;
                            }

                            model.nodeRemoved(root, idx, thread);
                        } else {
                            // load thread back.
                            // Load it back via SyncComplete packet
                            new SynchronizationCompletePacket(forumId, threadId, threadId).dispatch();
                        }
                    } else {
                        Post threadRoot = model.getRoot().getMessageById(threadId);
                        if (threadRoot != null) {
                            MessageData data = threadRoot.getMessageData();
                                threadRoot.setMessageData(data.setIgnored(p.isIgnored()));

                            model.subTreeNodesChanged(threadRoot);
                        }
                    }
                }
                }
        ).dispatch(p);
    }

    private static void hideReadThreads(SortedThreadsModel model) {
        boolean hideIgnored = Property.HIDE_IGNORED_TOPICS.get();
        boolean hideRead = Property.VIEW_THREAD_HIDE_READ_THREADS.get();

        ForumRoot root = (ForumRoot) model.getRoot();

        TIntList indexes = new TIntArrayList();
        List<Post> posts = new ArrayList<>();

        Iterator<Post> iterator = root.childrenPosts.iterator();
        int idx = 0;
        while (iterator.hasNext()) {
            Post threadRoot = iterator.next();

            if (
                    hideRead && threadRoot.isRead() == ReadStatus.Read ||
                            hideIgnored && threadRoot.isIgnored()
                    ) {
                iterator.remove();
                indexes.add(idx);
                posts.add(threadRoot);
            }
            idx++;
        }

        if (indexes.size() > 0) {
            model.fireNodesRemoved(root, indexes.toArray(), posts.toArray(new Post[posts.size()]));
        }
    }

    @Override
    public Icon getTitleIcon(SortedThreadsModel model) {
        if (model.getRoot() != null) {
            return ReadStatusIcon.Forum.getIcon(model.getRoot().isRead());
        }

        return null;
    }

    @Override
    public JPopupMenu getTitlePopup(SortedThreadsModel model, IAppControl appControl) {
        final MessageData data = model.getRoot().getMessageData();

        if (data instanceof ForumMessageData) {
            Forum f = ((ForumMessageData) data).getForum();

            return PopupMenuBuilder.getForumViewTabMenu(f, appControl);
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
            case IgnoreTopic:
                return post != null && !post.getThreadRoot().isIgnored();
            case FollowTopic:
                return post != null && post.getThreadRoot().isIgnored();
            case IgnoreUser:
                return post != null && !post.isIgnoredUser();
            case FollowUser:
                return post != null && post.isIgnoredUser();
            case ShowHidden:
                return Property.VIEW_THREAD_HIDE_READ_THREADS.get();
            case IgnoreUnread:
            case PreviousPost:
            case NextPost:
            case PreviousUnread:
            case NextUnread:
            case NewThread:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void unloadThread(SortedThreadsModel model, Post item) {
        Thread thread = (Thread) item;

        thread.clearThread();
        model.nodeStructureChanged(thread);
    }

    @Override
    public Property<OpenMessageMethod> getOpenMessageMethod() {
        return Property.OPEN_MESSAGE_BEHAVIOUR_FORUM_VIEW;
    }

    @Override
    public Post addPost(SortedThreadsModel model, Post root, MessageData data) {
        Post p;
        if (data.getTopicId() == 0) {
            assert root instanceof ForumRoot;
            p = new Thread(data, (ForumRoot) root);
        } else {
            p = new Post(data, root);
        }
        root.childrenPosts.add(p);

        Collections.sort(root.childrenPosts);

        model.nodeAdded(root, p);

        return p;
    }
}
