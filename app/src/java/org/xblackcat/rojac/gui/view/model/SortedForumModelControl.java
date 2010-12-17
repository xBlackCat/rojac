package org.xblackcat.rojac.gui.view.model;

import gnu.trove.TIntHashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.RojacDebugException;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.view.thread.IItemProcessor;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
 * Control class of all threads of the specified forum.
 *
 * @author xBlackCat
 */

public class SortedForumModelControl implements IModelControl<Post> {
    private static final Log log = LogFactory.getLog(SortedForumModelControl.class);

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    @Override
    public void fillModelByItemId(final AThreadModel<Post> model, int forumId) {
        assert RojacUtils.checkThread(true, getClass());

        final ForumRoot rootItem = new ForumRoot(forumId);

        model.setRoot(rootItem);

        new ForumInfoLoader(model, forumId).execute();
        new ThreadsLoader(forumId, rootItem, model).execute();
    }

    @Override
    public void markForumRead(AThreadModel<Post> model, boolean read) {
        assert RojacUtils.checkThread(true, getClass());

        // Root post is ForumRoot object
        model.getRoot().setRead(read);

        model.subTreeNodesChanged(model.getRoot());
    }

    @Override
    public void markThreadRead(AThreadModel<Post> model, int threadRootId, boolean read) {
        assert RojacUtils.checkThread(true, getClass());

        final Post post = model.getRoot().getMessageById(threadRootId);
        post.setDeepRead(read);

        model.subTreeNodesChanged(post);
    }

    @Override
    public void markPostRead(AThreadModel<Post> model, int postId, boolean read) {
        assert RojacUtils.checkThread(true, getClass());

        final Post post = model.getRoot().getMessageById(postId);
        if (post != null) {
            post.setRead(read);
            model.pathToNodeChanged(post);
        }
    }

    @Override
    public void loadThread(final AThreadModel<Post> threadModel, Post p, IItemProcessor<Post> postProcessor) {
        assert RojacUtils.checkThread(true, getClass());

        //  In the Sorted model only Thread object could be loaded.

        // Watch out for the line!
        final Thread item = (Thread) p;

        item.setLoadingState(LoadingState.Loading);

        new ThreadLoader(item, threadModel, postProcessor).execute();
    }

    @Override
    public boolean isRootVisible() {
        assert RojacUtils.checkThread(true, getClass());

        return false;
    }

    @Override
    public void updateModel(AThreadModel<Post> model, int... threadIds) {
        assert RojacUtils.checkThread(true, getClass());

        TIntHashSet filledThreads = new TIntHashSet();
        for (Post post : model.getRoot().getChildren()) {
            if (post.getThreadRoot().isFilled()) {
                filledThreads.add(post.getThreadRoot().getMessageId());
            }
        }

        // Retain only changed filled thread to reload.
        filledThreads.retainAll(threadIds);

        // Reload filled threads.
        for (int threadId : filledThreads.toArray()) {
            Post post = model.getRoot().getMessageById(threadId);
            if (post != null) {
                // Update thread children
                Thread t = post.getThreadRoot();

                if (t.isFilled()) {
                    // Thread is already filled - update children
                    loadThread(model, t, null);
                } else {
                    throw new RojacDebugException("Expected filled thread #" + threadId);
                }
            } else {
                throw new RojacDebugException("Thread #" + threadId + " not found");
            }
        }

        // Reload forum threads list.
        new ThreadsLoader(model.getRoot().getForumId(), (ForumRoot) model.getRoot(), model).execute();
    }

    @Override
    public String getTitle(AThreadModel<Post> model) {
        // Root is ForumRoot object
        Post root = model.getRoot();
        if (root.getMessageData() != null && root.getMessageData().getSubject() != null) {
            return root.getMessageData().getSubject();
        } else {
            return "#" + root.getForumId();
        }
    }

    @Override
    public boolean processPacket(final AThreadModel<Post> model, IPacket p) {
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
                new IPacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                        if (!p.isForumAffected(forumId)) {
                            // Current forum is not changed - have a rest
                            return;
                        }

                        updateModel(model, p.getThreadIds());
                    }
                }
        ).dispatch(p);
        return true;
    }

    private static class ForumInfoLoader extends RojacWorker<Void, Forum> {
        private final int forumId;
        private AThreadModel<Post> model;

        public ForumInfoLoader(AThreadModel<Post> model, int forumId) {
            this.forumId = forumId;
            this.model = model;
        }

        @Override
        protected Void perform() throws Exception {
            IForumAH fah = ServiceFactory.getInstance().getStorage().getForumAH();

            try {
                Forum forum = fah.getForumById(forumId);
                if (forum != null) {
                    publish(forum);
                }
            } catch (StorageException e) {
                log.error("Can not load forum information for forum id = " + forumId, e);
            }

            return null;
        }

        @Override
        protected void process(List<Forum> chunks) {
            for (Forum f : chunks) {
                MessageData fd = new MessageData(
                        -1,
                        -1,
                        -1,
                        forumId,
                        -1,
                        f.getForumName(),
                        f.getShortForumName(),
                        -1,
                        -1,
                        true,
                        null
                );
                model.getRoot().setMessageData(fd);
                model.nodeChanged(model.getRoot());
            }
        }
    }

}
