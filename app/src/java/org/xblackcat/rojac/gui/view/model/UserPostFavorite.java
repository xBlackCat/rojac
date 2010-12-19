package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageException;
import org.xblackcat.rojac.util.RojacUtils;

import java.util.Collection;

/**
 * @author xBlackCat
 */

class UserPostFavorite extends AnItemFavorite {
    private final IStorage storage = ServiceFactory.getInstance().getStorage();

    UserPostFavorite(Integer id, String config) {
        super(id, config);
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
        return Messages.Favorite_UserPosts_Name.get(userName);
    }

    @Override
    public Post getRootNode() throws StorageException {
        assert RojacUtils.checkThread(false, getClass());

        FavoritePostList root = new FavoritePostList(this);
        Collection<MessageData> messages = storage.getMessageAH().getUserPosts(itemId);
        root.fillList(messages);
        root.setLoadingState(LoadingState.Loaded);

        return root;
    }
}
