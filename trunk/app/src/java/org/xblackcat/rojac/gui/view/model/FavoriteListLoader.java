package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
* @author xBlackCat
*/
class FavoriteListLoader extends RojacWorker<Void, Post> {
    private final IFavorite favorite;
    private final AThreadModel<Post> model;

    public FavoriteListLoader(IFavorite favorite, AThreadModel<Post> model) {
        this.favorite = favorite;
        this.model = model;
    }

    @Override
    protected Void perform() throws Exception {
        Post newRoot = favorite.getRootNode();

        if (newRoot != null) {
            publish(newRoot);
        }

        return null;
    }

    @Override
    protected void process(List<Post> chunks) {
        if (chunks.size() != 1) {
            throw new IllegalStateException("Got more than one root node! " + chunks);
        }

        model.setRoot(chunks.remove(0));
    }
}