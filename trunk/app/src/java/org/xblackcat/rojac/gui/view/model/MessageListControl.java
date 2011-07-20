package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.view.thread.ThreadToolbarActions;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;

/**
 * @author xBlackCat
 */

abstract class MessageListControl implements IModelControl<Post> {
    private static final ThreadToolbarActions[] TOOLBAR_CONFIG = new ThreadToolbarActions[]{
            ThreadToolbarActions.PreviousPost,
            ThreadToolbarActions.NextPost,
            ThreadToolbarActions.PreviousUnread,
            ThreadToolbarActions.NextUnread,
    };

    @Override
    public void loadThread(AThreadModel<Post> model, Post item, Runnable postProcessor) {
        throw new NotImplementedException("The method shouldn't be used.");
    }

    @Override
    public boolean isRootVisible() {
        assert RojacUtils.checkThread(true);

        return false;
    }

    @Override
    public Post getTreeRoot(Post post) {
        // Post lists have no parents or children.
        return post;
    }

    @Override
    public JPopupMenu getItemMenu(Post post, IAppControl appControl) {
        return PopupMenuBuilder.getTreeViewMenu(post, appControl, false);
    }

    @Override
    public boolean allowSearch() {
        return false;
    }

    @Override
    public void resortModel(AThreadModel<Post> model) {
        // Nothing to do
    }

    @Override
    public ThreadToolbarActions[] getToolbar() {
        return TOOLBAR_CONFIG;
    }

    @Override
    public void unloadThread(AThreadModel<Post> postAThreadModel, Post item) {
        // Nothing to do
    }


    protected void markPostRead(AThreadModel<Post> model, int postId, boolean read) {
        assert RojacUtils.checkThread(true);

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

    protected abstract void updateModel(AThreadModel<Post> model, Runnable postProcessor);

    @Override
    public void processPacket(final AThreadModel<Post> model, IPacket p, final Runnable postProcessor) {
        new PacketDispatcher(
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
                        markPostRead(model, p.getPostId(), p.isRead());
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

    @Override
    public OpenMessageMethod getOpenMessageMethod() {
        return Property.OPEN_MESSAGE_BEHAVIOUR_POST_LIST.get();
    }
}
