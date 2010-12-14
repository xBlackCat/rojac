package org.xblackcat.rojac.data;

import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.gui.view.model.FavoriteType;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.service.storage.StorageException;

/**
 * Base interface for hold information about favorite. It contains own renderer.
 *
 * @author xBlackCat
 */

public interface IFavorite {
    void updateStatistic(Runnable runnable);

    FavoriteType getType();

    /**
     * Returns a favorite-specific config to store in database.
     *
     * @return parsable config string.
     */
    String getConfig();

    boolean isHighlighted();

    String getName();

    String getStatistic();

    int getId();

    /**
     * Initializes a root node for model. The node method shouldn't be invoked in SwingThread (from EventQueue) - it
     * could use accessing to database.
     *
     * @return generated and fully initialized root node.
     *
     * @throws org.xblackcat.rojac.RojacException if root node can't be constructed.
     *
     */
    Post getRootNode() throws RojacException;

    String loadName() throws StorageException;
}
