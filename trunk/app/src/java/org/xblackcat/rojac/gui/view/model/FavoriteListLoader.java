package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.util.RojacWorker;

import java.util.List;

/**
* @author xBlackCat
*/
class FavoriteListLoader extends RojacWorker<Void, Post> {
    private final Favorite favorite;
    private final AThreadModel<Post> model;

    public FavoriteListLoader(Runnable postProcessor, Favorite favorite, AThreadModel<Post> model) {
        super(postProcessor);
        this.favorite = favorite;
        this.model = model;
    }

    @Override
    protected Void perform() throws Exception {
        Post newRoot = favorite.getType().makeRootNode(favorite.getItemId());

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
