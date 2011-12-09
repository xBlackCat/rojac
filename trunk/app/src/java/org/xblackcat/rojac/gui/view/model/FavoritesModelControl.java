package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.NotImplementedException;
import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.theme.ViewIcon;
import org.xblackcat.rojac.gui.view.thread.ThreadToolbarActions;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.storage.IFavoriteAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;

/**
 * @author xBlackCat
 */

class FavoritesModelControl implements IModelControl {
    private IModelControl delegatedControl = null;
    private String title = null;

    private ReadStatusIcon statusIcons = ReadStatusIcon.Favorite;

    @Override
    public void fillModelByItemId(SortedThreadsModel model, int itemId) {
        assert RojacUtils.checkThread(true);

        title = "Favorite #" + itemId;

        new FavoriteLoader(model, itemId).execute();
    }

    @Override
    public void loadThread(SortedThreadsModel model, Post item, Runnable postProcessor) {
        assert RojacUtils.checkThread(true);

        throw new NotImplementedException("The method shouldn't be invoked.");
    }

    @Override
    public boolean isRootVisible() {
        assert RojacUtils.checkThread(true);

        return delegatedControl != null && delegatedControl.isRootVisible();
    }

    @Override
    public String getTitle(SortedThreadsModel model) {
        assert RojacUtils.checkThread(true);

        if (title == null) {
            return "Favorite";
        } else {
            return title;
        }
    }

    @Override
    public void processPacket(SortedThreadsModel model, IPacket p, Runnable postProcessor) {
        if (delegatedControl != null) {
            delegatedControl.processPacket(model, p, postProcessor);
        }
    }

    @Override
    public Post getTreeRoot(Post post) {
        return delegatedControl == null ? null : delegatedControl.getTreeRoot(post);
    }

    @Override
    public JPopupMenu getItemMenu(Post post, IAppControl appControl) {
        return delegatedControl == null ? null : delegatedControl.getItemMenu(post, appControl);
    }

    @Override
    public boolean allowSearch() {
        return false;
    }

    @Override
    public void resortModel(SortedThreadsModel model) {
        if (delegatedControl != null) {
            delegatedControl.resortModel(model);
        }
    }

    @Override
    public Icon getTitleIcon(SortedThreadsModel model) {
        if (model.getRoot() == null) {
            return ViewIcon.Favorites;
        }

        return statusIcons.getIcon(model.getRoot().isRead());
    }

    @Override
    public JPopupMenu getTitlePopup(SortedThreadsModel model, IAppControl appControl) {
        if (delegatedControl == null) {
            return null;
        } else {
            switch (statusIcons) {
                case FavoriteThread:
                    return PopupMenuBuilder.getThreadViewTabMenu(model.getRoot(), appControl, false);
                case FavoritePostList:
                case FavoriteResponseList:
                    return PopupMenuBuilder.getFavoriteMessagesListTabMenu(model.getRoot(), appControl);
            }
            return null;
        }
    }

    @Override
    public ThreadToolbarActions[] getToolbar() {
        return delegatedControl == null ? null : delegatedControl.getToolbar();
    }

    @Override
    public void unloadThread(SortedThreadsModel model, Post item) {
        if (delegatedControl != null) {
            delegatedControl.unloadThread(model, item);
        }
    }

    @Override
    public void onDoubleClick(Post post, IAppControl appControl) {
        if (delegatedControl != null) {
            delegatedControl.onDoubleClick(post, appControl);
        }
    }

    /**
     * Util class to determine favorite type and the control behaviour.
     */
    private class FavoriteLoader extends RojacWorker<Void, Void> {
        private final SortedThreadsModel model;
        private final int favoriteId;

        private String name;
        private Post root;
        private FavoriteType type;

        public FavoriteLoader(SortedThreadsModel model, int favoriteId) {
            this.model = model;
            this.favoriteId = favoriteId;
        }

        @Override
        protected Void perform() throws Exception {
            IFavoriteAH fAH = Storage.get(IFavoriteAH.class);

            Favorite f = fAH.getFavorite(favoriteId);

            if (f != null) {
                type = f.getType();
                int itemId = f.getItemId();

                root = type.makeRootNode(itemId);
                name = type.loadName(itemId);
            }

            return null;
        }

        @Override
        protected void done() {
            if (type == null) {
                model.setRoot(null);
                return;
            }

            title = name;

            // Set proper delegated model control
            delegatedControl = type.getModelControl().get();
            statusIcons = type.getIcons();

            // Fill model with correspond data.
            model.setRoot(root);
        }
    }
}
