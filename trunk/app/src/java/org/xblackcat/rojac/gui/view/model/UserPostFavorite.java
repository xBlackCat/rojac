package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;

/**
 * @author xBlackCat
 */

class UserPostFavorite extends AFavorite {
    UserPostFavorite(Integer id, int itemId) {
        super(id, itemId);
    }

    @Override
    public FavoriteType getType() {
        return FavoriteType.UserPosts;
    }

    @Override
    protected FavoriteStatData loadStatistic() throws StorageException {
        return storage.getMessageAH().getUserPostsStat(itemId);
    }

    @Override
    public String loadName() throws StorageException {
        User user = storage.getUserAH().getUserById(itemId);
        String userName = user != null ? user.getUserNick() : "#" + itemId;
        return Message.Favorite_UserPosts_Name.get(userName);
    }

    @Override
    public Post getRootNode() throws StorageException {
        assert RojacUtils.checkThread(false);

        PostList root = new PostList(itemId);
        Iterable<MessageData> messages = storage.getMessageAH().getUserPosts(itemId);
        root.fillList(messages);
        root.setLoadingState(LoadingState.Loaded);

        return root;
    }
}
