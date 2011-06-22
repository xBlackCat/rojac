package org.xblackcat.rojac.gui.view.model;

import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat
 */

public enum FavoriteType {
    Thread(Message.Favorite_Thread_Name) {
        @Override
        protected IFavorite createFavorite(Integer id, String config) {
            return new ThreadFavorite(id, config);
        }
    },
    UserPosts(Message.Favorite_UserPosts_Name) {
        @Override
        protected IFavorite createFavorite(Integer id, String config) {
            return new UserPostFavorite(id, config);
        }
    },
    SubThread(Message.Favorite_SubTree_Name) {
        @Override
        protected IFavorite createFavorite(Integer id, String config) {
            return new SubThreadFavorite(id, config);
        }
    },
    UserResponses(Message.Favorite_UserReplies_Name) {
        @Override
        protected IFavorite createFavorite(Integer id, String config) {
            return new UserResponseFavorite(id, config);
        }
    },
    Category(Message.Favorite_Category_Name) {
        @Override
        protected IFavorite createFavorite(Integer id, String config) {
            return new CategoryFavorite(id, config);
        }
    };

    private final Message typeName;

    FavoriteType(Message typeName) {
        this.typeName = typeName;
    }

    public String getTypeName(Object... args) {
        return typeName.get(args);
    }

    protected abstract IFavorite createFavorite(Integer id, String config);

    /**
     * Restores a favorite, basing on information
     *
     * @param id
     * @param type
     * @param config
     *
     * @return
     *
     * @throws IllegalArgumentException if this favorite type is not exists
     */
    public static IFavorite restoreFavorite(int id, String type, String config) throws IllegalArgumentException {
        return valueOf(type).createFavorite(id, config);
    }
}
