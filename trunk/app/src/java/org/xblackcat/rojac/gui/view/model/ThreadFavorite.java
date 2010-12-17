package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageDataException;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;

import java.util.Arrays;

/**
 * @author xBlackCat
 */

class ThreadFavorite extends AnItemFavorite {
    ThreadFavorite(Integer id, String config) {
        super(id, config);
    }

    @Override
    public FavoriteType getType() {
        return FavoriteType.Thread;
    }

    @Override
    protected int loadAmount() throws StorageException {
        return ServiceFactory.getInstance().getStorage().getMessageAH().getUnreadReplaysInThread(itemId);
    }

    @Override
    public String loadName() throws StorageException {
        IMessageAH mAH = ServiceFactory.getInstance().getStorage().getMessageAH();
        MessageData md = mAH.getMessageData(itemId);
        return "Topic " + md.getSubject();
    }

    @Override
    public Post getRootNode() throws StorageException {
        assert RojacUtils.checkThread(false, getClass());
        IStorage storage = ServiceFactory.getInstance().getStorage();
        IMessageAH mAH = storage.getMessageAH();

        MessageData messageData = mAH.getMessageData(itemId);
        if (messageData == null) {
            throw new StorageDataException("Thread root not found #" + itemId);
        }

        MessageData[] messages = mAH.getMessagesDataByTopicId(itemId, messageData.getForumId());
        Thread root = new Thread(messageData, null, 0, null);
        root.fillThread(Arrays.asList(messages));
        root.setLoadingState(LoadingState.Loaded);

        return root;
    }
}
