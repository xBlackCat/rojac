package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.view.thread.IItemProcessor;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public class MessageListControl implements IModelControl<Post> {
    @Override
    public void fillModelByItemId(AThreadModel<Post> model, int itemId) {
        throw new NotImplementedException("The method shouldn't be used.");
    }

    private void markPostRead(AThreadModel<Post> model, int postId, boolean read) {
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

    @Override
    public void loadThread(AThreadModel<Post> model, Post item, IItemProcessor<Post> postProcessor) {
        throw new NotImplementedException("The method shouldn't be used.");
    }

    @Override
    public boolean isRootVisible() {
        assert RojacUtils.checkThread(true);

        return false;
    }

    private void updateModel(final AThreadModel<Post> model, int... threadIds) {
        assert RojacUtils.checkThread(true);

        // Parent in the case is FavoritePostList object.
        FavoritePostList root = (FavoritePostList) model.getRoot();

        final IFavorite favorite = root.getFavorite();

        new FavoriteListLoader(favorite, model).execute();
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
        // Post lists have no parents or children.
        return post;
    }

    @Override
    public JPopupMenu getItemMenu(Post post, IAppControl appControl) {
        return PopupMenuBuilder.getTreeViewMenu(post, appControl, true, false);
    }

    @Override
    public boolean allowSearch() {
        return false;
    }

    @Override
    public void resortModel(AThreadModel<Post> postAThreadModel) {
        // Nothing to do
    }

    @Override
    public JPopupMenu getTitlePopup(AThreadModel<Post> postAThreadModel, IAppControl appControl) {
        return null;
    }

    @Override
    public Icon getTitleIcon(AThreadModel<Post> postAThreadModel) {
        return null;
    }
}
