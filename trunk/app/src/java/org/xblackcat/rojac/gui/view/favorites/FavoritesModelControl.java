package org.xblackcat.rojac.gui.view.favorites;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.gui.view.model.AThreadModel;
import org.xblackcat.rojac.gui.view.model.IModelControl;
import org.xblackcat.rojac.gui.view.model.MessageListControl;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.model.SingleModelControl;
import org.xblackcat.rojac.gui.view.thread.IItemProcessor;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IFavoriteAH;
import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.rojac.util.RojacWorker;

/**
 * @author xBlackCat
 */

public class FavoritesModelControl implements IModelControl<Post> {
    private IModelControl<Post> delegatedControl = null;
    private String title = null;

    @Override
    public void fillModelByItemId(AThreadModel<Post> model, int itemId) {
        assert RojacUtils.checkThread(true, getClass());

        new FavoriteLoader(model, itemId).execute();
    }

    @Override
    public void markForumRead(AThreadModel<Post> model, boolean read) {
        assert RojacUtils.checkThread(true, getClass());

        if (delegatedControl != null) {
            delegatedControl.markForumRead(model, read);
        }
    }

    @Override
    public void markThreadRead(AThreadModel<Post> model, int threadRootId, boolean read) {
        assert RojacUtils.checkThread(true, getClass());

        if (delegatedControl != null) {
            delegatedControl.markThreadRead(model, threadRootId, read);
        }
    }

    @Override
    public void markPostRead(AThreadModel<Post> model, int postId, boolean read) {
        assert RojacUtils.checkThread(true, getClass());

        if (delegatedControl != null) {
            delegatedControl.markPostRead(model, postId, read);
        }
    }

    @Override
    public void loadThread(AThreadModel<Post> model, Post item, IItemProcessor<Post> postProcessor) {
        assert RojacUtils.checkThread(true, getClass());

        throw new NotImplementedException("The method shouldn't be invoked.");
    }

    @Override
    public boolean isRootVisible() {
        assert RojacUtils.checkThread(true, getClass());

        return delegatedControl != null && delegatedControl.isRootVisible();
    }

    @Override
    public void updateModel(AThreadModel<Post> model, int... threadIds) {
        assert RojacUtils.checkThread(true, getClass());

        if (delegatedControl != null) {
            delegatedControl.updateModel(model, threadIds);
        }
    }

    @Override
    public String getTitle(AThreadModel<Post> model) {
        assert RojacUtils.checkThread(true, getClass());

        if (title == null) {
            return "Favorite";
        } else {
            return title;
        }
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

            root = f.getRootNode();
            name = f.loadName();

            return null;
        }

        @Override
        protected void done() {
            title = name;

            // Set proper ThreadsControl
            switch (f.getType()) {
                case UnreadUserResponses:
                case UnreadUserPosts:
                case Category:
                    delegatedControl = new MessageListControl();
                    break;
                case UnreadPostResponses:
                case UnreadPostsInThread:
                    delegatedControl = new SingleModelControl();
                    break;
            }

            // Fill model with correspond data.
            model.setRoot(root);

        }
    }

}
