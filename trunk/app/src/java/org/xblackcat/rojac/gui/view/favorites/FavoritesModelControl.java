package org.xblackcat.rojac.gui.view.favorites;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.theme.FavoritesIcon;
import org.xblackcat.rojac.gui.theme.IconPack;
import org.xblackcat.rojac.gui.theme.ViewIcon;
import org.xblackcat.rojac.gui.view.model.*;
import org.xblackcat.rojac.gui.view.thread.IItemProcessor;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.datahandler.IPacket;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IFavoriteAH;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author xBlackCat
 */

public class FavoritesModelControl implements IModelControl<Post> {
    private IModelControl<Post> delegatedControl = null;
    private String title = null;

    private Map<ReadStatus, FavoritesIcon> statusIcons = new EnumMap<ReadStatus, FavoritesIcon>(ReadStatus.class);

    @Override
    public void fillModelByItemId(AThreadModel<Post> model, int itemId) {
        assert RojacUtils.checkThread(true);

        title = "Favorite #" + itemId;

        new FavoriteLoader(model, itemId).execute();
    }

    @Override
    public void loadThread(AThreadModel<Post> model, Post item, IItemProcessor<Post> postProcessor) {
        assert RojacUtils.checkThread(true);

        throw new NotImplementedException("The method shouldn't be invoked.");
    }

    @Override
    public boolean isRootVisible() {
        assert RojacUtils.checkThread(true);

        return delegatedControl != null && delegatedControl.isRootVisible();
    }

    @Override
    public String getTitle(AThreadModel<Post> model) {
        assert RojacUtils.checkThread(true);

        if (title == null) {
            return "Favorite";
        } else {
            return title;
        }
    }

    @Override
    public boolean processPacket(AThreadModel<Post> model, IPacket p) {
        return delegatedControl != null && delegatedControl.processPacket(model, p);
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
    public void resortModel(AThreadModel<Post> model) {
        if (delegatedControl != null) {
            delegatedControl.resortModel(model);
        }
    }

    @Override
    public Icon getTitleIcon(AThreadModel<Post> model) {
        IconPack imagePack = Property.ROJAC_GUI_ICONPACK.get();
        if (model.getRoot() == null) {
            return imagePack.getIcon(ViewIcon.Favorites);
        }

        FavoritesIcon icon = statusIcons.get(model.getRoot().isRead());
        return imagePack.getIcon(icon);
    }

    @Override
    public JPopupMenu getTitlePopup(AThreadModel<Post> model, IAppControl appControl) {
        return delegatedControl == null ? null : delegatedControl.getTitlePopup(model, appControl);
    }

    /**
     * Util class to determine favorite type and the control behaviour.
     */
    private class FavoriteLoader extends RojacWorker<Void, Void> {
        private final AThreadModel<Post> model;
        private final int favoriteId;

        private IFavorite f;
        private String name;
        private Post root;

        public FavoriteLoader(AThreadModel<Post> model, int favoriteId) {
            this.model = model;
            this.favoriteId = favoriteId;
        }

        @Override
        protected Void perform() throws Exception {
            IFavoriteAH fAH = ServiceFactory.getInstance().getStorage().getFavoriteAH();

            f = fAH.getFavorite(favoriteId);

            if (f != null) {
                root = f.getRootNode();
                name = f.loadName();
            }

            return null;
        }

        @Override
        protected void done() {
            if (f == null) {
                model.setRoot(null);
                return;
            }

            title = name;

            // Set proper ThreadsControl
            switch (f.getType()) {
                case UserResponses:
                case UserPosts:
                case Category:
                    delegatedControl = new MessageListControl();
                    statusIcons.put(ReadStatus.Read, FavoritesIcon.UserPostsRead);
                    statusIcons.put(ReadStatus.ReadPartially, FavoritesIcon.UserPostsReadPartially);
                    statusIcons.put(ReadStatus.Unread, FavoritesIcon.UserPostsUnread);
                    break;
                case SubThread:
                case Thread:
                    delegatedControl = new SingleModelControl();
                    statusIcons.put(ReadStatus.Read, FavoritesIcon.ThreadRead);
                    statusIcons.put(ReadStatus.ReadPartially, FavoritesIcon.ThreadReadPartially);
                    statusIcons.put(ReadStatus.Unread, FavoritesIcon.ThreadUnread);
                    break;
            }

            // Fill model with correspond data.
            model.setRoot(root);

        }
    }
}
