package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.util.RojacUtils;

import javax.swing.*;

/**
 * @author xBlackCat
 */
class FavoritesMessageListControl extends MessageListControl {
    @Override
    public void fillModelByItemId(AThreadModel<Post> model, int itemId) {
        throw new NotImplementedException("The method shouldn't be used.");
    }

    @Override
    public JPopupMenu getTitlePopup(AThreadModel<Post> model, IAppControl appControl) {
        Post root = model.getRoot();

        return PopupMenuBuilder.getFavoriteMessagesListTabMenu(root, appControl);
    }

    @Override
    public Icon getTitleIcon(AThreadModel<Post> model) {
        return null;
    }

    @Override
    protected void updateModel(final AThreadModel<Post> model, Runnable postProcessor) {
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

}
