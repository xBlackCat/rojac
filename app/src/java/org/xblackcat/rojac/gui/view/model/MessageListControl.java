package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.gui.view.thread.IItemProcessor;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.datahandler.IPacketProcessor;
import org.xblackcat.rojac.service.datahandler.PacketDispatcher;
import org.xblackcat.rojac.service.datahandler.SetForumReadPacket;
import org.xblackcat.rojac.service.datahandler.SetPostReadPacket;
import org.xblackcat.rojac.service.datahandler.SynchronizationCompletePacket;
import org.xblackcat.rojac.util.RojacUtils;

/**
 * @author xBlackCat
 */

public class MessageListControl implements IModelControl<Post> {
    @Override
    public void fillModelByItemId(AThreadModel<Post> model, int itemId) {
        throw new NotImplementedException("The method shouldn't be used.");
    }

    private void markPostRead(AThreadModel<Post> model, int postId, boolean read) {
        assert RojacUtils.checkThread(true, getClass());

        Post p = model.getRoot().getMessageById(postId);

        if (p == null) {
            return;
        }

        boolean postRead = p.isRead() != ReadStatus.Unread;
        if (postRead != read) {
            p.setRead(read);
            model.nodeChanged(p);
        }
    }

    @Override
    public void loadThread(AThreadModel<Post> model, Post item, IItemProcessor<Post> postProcessor) {
        throw new NotImplementedException("The method shouldn't be used.");
    }

    @Override
    public boolean isRootVisible() {
        assert RojacUtils.checkThread(true, getClass());

        return false;
    }

    private void updateModel(AThreadModel<Post> model, int... threadIds) {
        assert RojacUtils.checkThread(true, getClass());

       // TODO: implement reloading list information. Make it customizable.
    }

    @Override
    public String getTitle(AThreadModel<Post> model) {
        throw new NotImplementedException("The method shouldn't be used.");
    }

    @Override
    public boolean processPacket(final AThreadModel<Post> model, IPacket p) {
        new PacketDispatcher(
                new IPacketProcessor<SetForumReadPacket>() {
                    @Override
                    public void process(SetForumReadPacket p) {
                        updateModel(model);
                    }
                },
                new IPacketProcessor<SetPostReadPacket>() {
                    @Override
                    public void process(SetPostReadPacket p) {
                        if (p.isRecursive()) {
                            // Post is a root of marked thread
                            updateModel(model);
                        } else {
                            // Mark as read only the post
                            markPostRead(model, p.getPostId(), p.isRead());
                        }
                    }
                },
                new IPacketProcessor<SynchronizationCompletePacket>() {
                    @Override
                    public void process(SynchronizationCompletePacket p) {
                        updateModel(model);
                    }
                }
        ).dispatch(p);
        return true;
    }

    @Override
    public Post getTreeRoot(Post post) {
        // Parent in the case is PostList object.
        return post;
    }
}
