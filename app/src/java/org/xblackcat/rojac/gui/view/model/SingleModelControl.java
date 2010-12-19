package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.gui.view.MessageChecker;
import org.xblackcat.rojac.gui.view.thread.IItemProcessor;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
import org.xblackcat.rojac.service.datahandler.PacketDispatcher;
import org.xblackcat.rojac.service.datahandler.SetForumReadPacket;
import org.xblackcat.rojac.service.datahandler.SetPostReadPacket;
import org.xblackcat.rojac.service.datahandler.SynchronizationCompletePacket;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.RojacUtils;

/**
 * Control class of all threads of the specified forum.
 *
 * @author xBlackCat
 */

public class SingleModelControl implements IModelControl<Post> {
    private static final Log log = LogFactory.getLog(SingleModelControl.class);

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();

    @Override
    public void fillModelByItemId(final AThreadModel<Post> model, int threadId) {
        assert RojacUtils.checkThread(true, getClass());

        MessageData fakeMessageData = new MessageData(threadId, 0, 0, 0, 0, null, "Loading...", 0L, 0L, false, null);
        final Thread rootItem = new Thread(fakeMessageData, null);

        model.setRoot(rootItem);

        new MessageChecker(threadId) {
            @Override
            protected void done() {
                rootItem.setMessageData(data);
                model.nodeChanged(rootItem);

                loadThread(model, rootItem, null);
            }
        }.execute();
    }

    private void markForumRead(AThreadModel<Post> model, boolean read) {
        assert RojacUtils.checkThread(true, getClass());

        // Root post is Thread object
        model.getRoot().setDeepRead(read);

        model.subTreeNodesChanged(model.getRoot());
    }

    private void markThreadRead(AThreadModel<Post> model, int threadRootId, boolean read) {
        assert RojacUtils.checkThread(true, getClass());

        Post post = model.getRoot().getMessageById(threadRootId);
        if (post != null) {
            post.setDeepRead(read);

            model.pathToNodeChanged(post);
            model.subTreeNodesChanged(post);
        }
    }

    private void markPostRead(AThreadModel<Post> model, int postId, boolean read) {
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

        return true;
    }

    @Override
    public String getTitle(AThreadModel<Post> model) {
        assert RojacUtils.checkThread(true, getClass());

        // Root is Thread object
        Post root = model.getRoot();
        if (root.getMessageData() != null && root.getMessageData().getSubject() != null) {
            return root.getMessageData().getSubject();
        } else {
            return "#" + root.getMessageId();
        }
    }

    @Override
    public boolean processPacket(final AThreadModel<Post> model, IPacket p) {
        final int forumId = model.getRoot().getForumId();
        final int threadId = model.getRoot().getMessageId();

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
                        if (!p.isTopicAffected(threadId)) {
                            return;
                        }

                        // Thread always filled in.
                        loadThread(model, model.getRoot(), null);
                    }
                }
        ).dispatch(p);
        return true;
    }

    @Override
    public Post getTreeRoot(Post post) {
        return post == null ? null : post.getThreadRoot();
    }
}
