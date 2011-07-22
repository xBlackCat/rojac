package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;

/**
 * @author xBlackCat
 */

class CategoryFavorite extends AFavorite {
    CategoryFavorite(Integer id, int itemId) {
        super(id, itemId);
    }

    @Override
    public FavoriteType getType() {
        return FavoriteType.Category;
    }

    @Override
    protected FavoriteStatData loadStatistic() throws StorageException {
        IMessageAH messageAH = ServiceFactory.getInstance().getStorage().getMessageAH();
        return messageAH.getReplaysInThread(itemId);
    }

    @Override
    public String loadName() throws StorageException {
        return Message.Favorite_Category_Name.get("#" + itemId);
    }

    @Override
    public Post getRootNode() {
        assert RojacUtils.checkThread(false);

        throw new NotImplementedException();
    }
}
