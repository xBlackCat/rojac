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

class UnreadPostsInThreadFavorite extends AnItemFavorite {
    UnreadPostsInThreadFavorite(Integer id, String config) {
        super(id, config);
    }

    @Override
    public FavoriteType getType() {
        return FavoriteType.UnreadPostsInThread;
    }

    @Override
    protected int loadAmount() throws StorageException {
        return ServiceFactory.getInstance().getStorage().getMessageAH().getUnreadReplaysInThread(itemId);
    }

    @Override
    protected String loadName() throws StorageException {
        return "Unread posts in thread #" + id;
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
