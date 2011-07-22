package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;

/**
 * @author xBlackCat
 */

class SubThreadFavorite extends AFavorite {
    protected final IMessageAH messageAH = storage.getMessageAH();

    SubThreadFavorite(Integer id, int itemId) {
        super(id, itemId);
    }

    @Override
    public FavoriteType getType() {
        return FavoriteType.SubThread;
    }

    @Override
    protected FavoriteStatData loadStatistic() throws StorageException {
        return messageAH.getReplaysInThread(itemId);
    }

    @Override
    public String loadName() throws StorageException {
        MessageData md = messageAH.getMessageData(itemId);
        String subject = md != null ? md.getSubject() : "#" + itemId;
        return Message.Favorite_SubTree_Name.get(subject);
    }

    @Override
    public Post getRootNode() {
        assert RojacUtils.checkThread(false);

        throw new NotImplementedException();
    }
}
