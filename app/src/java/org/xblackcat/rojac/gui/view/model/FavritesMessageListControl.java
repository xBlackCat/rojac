package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.OpenMessageMethod;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.service.datahandler.*;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;

/**
 * @author xBlackCat
 */
class FavritesMessageListControl extends MessageListControl {
    @Override
    public void fillModelByItemId(AThreadModel<Post> model, int itemId) {
        throw new NotImplementedException("The method shouldn't be used.");
    }

    @Override
    public JPopupMenu getTitlePopup(AThreadModel<Post> model, IAppControl appControl) {
        Post root = model.getRoot();

        return PopupMenuBuilder.getMessagesListTabMenu(root, appControl);
    }

    @Override
    public Icon getTitleIcon(AThreadModel<Post> model) {
        return null;
    }

    private void updateModel(final AThreadModel<Post> model, Runnable postProcessor) {
        assert RojacUtils.checkThread(true);

        // Parent in the case is FavoritePostList object.
        FavoritePostList root = (FavoritePostList) model.getRoot();

        final IFavorite favorite = root.getFavorite();

        new FavoriteListLoader(postProcessor, favorite, model).execute();
    }

    @Override
    public String getTitle(AThreadModel<Post> model) {
        throw new NotImplementedException("The method shouldn't be used.");
    }

    @Override
    public void processPacket(final AThreadModel<Post> model, IPacket p, final Runnable postProcessor) {
        new PacketDispatcher(
                new IPacketProcessor<SetForumReadPacket>() {
                    @Override
                    public void process(SetForumReadPacket p) {
                        updateModel(model, postProcessor);
                    }
                },
                new IPacketProcessor<SetPostReadPacket>() {
                    @Override
                    public void process(SetPostReadPacket p) {
                        if (p.isRecursive()) {
                            // Post is a root of marked thread
                            updateModel(model, postProcessor);
                        } else {
                            // Mark as read only the post
                            markPostRead(model, p.getPostId(), p.isRead());
                        }
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
        return OpenMessageMethod.InThread;
    }
}
