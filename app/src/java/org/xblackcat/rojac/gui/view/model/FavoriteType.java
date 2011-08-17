package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.NotImplementedException;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.data.MessageData;
import org.xblackcat.rojac.data.User;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.service.storage.StorageDataException;
import org.xblackcat.rojac.util.RojacUtils;

import java.util.Collection;

/**
 * @author xBlackCat
 */

public enum FavoriteType {
    Thread(Message.Favorite_Thread_Name, ModelControl.SingleThread, ReadStatusIcon.FavoriteThread) {
        @Override
        protected Post makeRootNode(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            MessageData messageData = storage.getMessageAH().getMessageData(itemId);
            if (messageData == null) {
                throw new StorageDataException("Thread root not found #" + itemId);
            }

            Collection<MessageData> messages = storage.getMessageAH().getMessagesDataByTopicId(itemId, messageData.getForumId());
            Thread root = new Thread(messageData, null, 0, null);
            root.fillThread(messages);
            root.setLoadingState(LoadingState.Loaded);

            return root;
        }

        @Override
        public String loadName(int itemId) throws RojacException {
            MessageData md = storage.getMessageAH().getMessageData(itemId);
            String subject = md != null ? md.getSubject() : "#" + itemId;
            return Message.Favorite_Thread_Name.get(subject);
        }

        @Override
        public FavoriteStatData loadStatistic(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            return storage.getStatisticAH().getReplaysInThread(itemId);
        }
    },
    UserPosts(Message.Favorite_UserPosts_Name, ModelControl.UserPosts, ReadStatusIcon.FavoritePostList) {
        @Override
        protected Post makeRootNode(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            PostList root = new PostList(itemId);
            Iterable<MessageData> messages = storage.getMessageAH().getUserPosts(itemId);
            root.fillList(messages);
            root.setLoadingState(LoadingState.Loaded);

            return root;
        }

        @Override
        public String loadName(int itemId) throws RojacException {
            User user = storage.getUserAH().getUserById(itemId);
            String userName = user != null ? user.getUserNick() : "#" + itemId;
            return Message.Favorite_UserPosts_Name.get(userName);
        }

        @Override
        public FavoriteStatData loadStatistic(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            return storage.getStatisticAH().getUserPostsStat(itemId);
        }
    },
    SubThread(Message.Favorite_SubTree_Name, ModelControl.SingleThread, ReadStatusIcon.FavoriteThread) {
        @Override
        protected Post makeRootNode(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            throw new NotImplementedException();
        }

        @Override
        public String loadName(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            MessageData md = storage.getMessageAH().getMessageData(itemId);
            String subject = md != null ? md.getSubject() : "#" + itemId;
            return Message.Favorite_SubTree_Name.get(subject);
        }

        @Override
        public FavoriteStatData loadStatistic(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            return storage.getStatisticAH().getReplaysInThread(itemId);
        }
    },
    UserResponses(Message.Favorite_UserReplies_Name, ModelControl.UserReplies, ReadStatusIcon.FavoriteResponseList) {
        @Override
        protected Post makeRootNode(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            PostList root = new PostList(itemId);
            Collection<MessageData> messages = storage.getMessageAH().getUserReplies(itemId);
            root.fillList(messages);
            root.setLoadingState(LoadingState.Loaded);

            return root;
        }

        @Override
        public String loadName(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            User user = storage.getUserAH().getUserById(itemId);
            String userName = user != null ? user.getUserNick() : "user #" + itemId;
            return Message.Favorite_UserReplies_Name.get(userName);
        }

        @Override
        public FavoriteStatData loadStatistic(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            return storage.getStatisticAH().getUserRepliesStat(itemId);
        }
    },
    Category(Message.Favorite_Category_Name, ModelControl.SingleThread, ReadStatusIcon.FavoritePostList) {
        @Override
        protected Post makeRootNode(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            throw new NotImplementedException();
        }

        @Override
        public String loadName(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            return Message.Favorite_Category_Name.get("#" + itemId);
        }

        @Override
        public FavoriteStatData loadStatistic(int itemId) throws RojacException {
            assert RojacUtils.checkThread(false);

            throw new NotImplementedException();
        }
    };

    protected final IStorage storage = ServiceFactory.getInstance().getStorage();
    private final Message typeName;
    private final ModelControl modelControl;
    private final ReadStatusIcon icons;

    FavoriteType(Message typeName, ModelControl modelControl, ReadStatusIcon icons) {
        this.typeName = typeName;
        this.modelControl = modelControl;
        this.icons = icons;
    }

    public String getTypeName(Object... args) {
        return typeName.get(args);
    }

    public ModelControl getModelControl() {
        return modelControl;
    }

    public ReadStatusIcon getIcons() {
        return icons;
    }

    /**
     * Initializes a root node for model. The node method shouldn't be invoked in SwingThread (from EventQueue) - it
     * could use accessing to database.
     *
     * @param itemId item id (type depends on favorite types)
     * @return generated and fully initialized root node.
     * @throws org.xblackcat.rojac.RojacException
     *          if root node can't be constructed.
     */
    protected abstract Post makeRootNode(int itemId) throws RojacException;

    public abstract String loadName(int itemId) throws RojacException;

    public abstract FavoriteStatData loadStatistic(int itemId) throws RojacException;

}
