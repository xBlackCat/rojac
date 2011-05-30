package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IMessageAH;
import org.xblackcat.rojac.service.storage.StorageDataException;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;

import java.util.Collection;

/**
 * @author xBlackCat
 */

class ThreadFavorite extends AnItemFavorite {
    protected final IMessageAH messageAH = ServiceFactory.getInstance().getStorage().getMessageAH();

    ThreadFavorite(Integer id, String config) {
        super(id, config);
    }

    @Override
    public FavoriteType getType() {
        return FavoriteType.Thread;
    }

    @Override
    protected FavoriteStatData loadStatistic() throws StorageException {
        return messageAH.getReplaysInThread(itemId);
    }

    @Override
    public String loadName() throws StorageException {
        MessageData md = messageAH.getMessageData(itemId);
        String subject = md != null ? md.getSubject() : "#" + itemId;
        return Messages.Favorite_Thread_Name.get(subject);
    }

    @Override
    public Post getRootNode() throws StorageException {
        assert RojacUtils.checkThread(false);

        MessageData messageData = messageAH.getMessageData(itemId);
        if (messageData == null) {
            throw new StorageDataException("Thread root not found #" + itemId);
        }

        Collection<MessageData> messages = messageAH.getMessagesDataByTopicId(itemId, messageData.getForumId());
        Thread root = new Thread(messageData, null, 0, null);
        root.fillThread(messages);
        root.setLoadingState(LoadingState.Loaded);

        return root;
    }
}