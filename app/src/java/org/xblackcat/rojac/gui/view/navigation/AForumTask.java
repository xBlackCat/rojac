package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.rojac.service.storage.IForumAH;
import org.xblackcat.rojac.service.storage.Storage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;

/**
 * @author xBlackCat
 */
abstract class AForumTask<V> extends ALoadTask<V> {
    protected final ForumStatistic getForumStatistic(int forumId) throws StorageException {
        assert RojacUtils.checkThread(false);

        final IForumAH fah = Storage.get(IForumAH.class);

        return fah.getForumStatistic(forumId, Property.RSDN_USER_ID.get(-1));
    }
}
