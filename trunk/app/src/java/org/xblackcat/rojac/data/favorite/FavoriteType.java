package org.xblackcat.rojac.data.favorite;

import org.xblackcat.rojac.i18n.Messages;

/**
 * @author xBlackCat
 */

public enum FavoriteType {
    UnreadPostsInThread(Messages.Favorite_Name_UnreadTreadPosts) {
        @Override
        protected IFavorite createFavorite(Integer id, String name, String config) {
            return new UnreadPostsInThreadFavorite(id, name, config);
        }
    },
    UnreadUserPosts(Messages.Favorite_Name_UserPosts) {
        @Override
        protected IFavorite createFavorite(Integer id, String name, String config) {
            return new UnreadUserPostsFavorite(id, name, config);
        }
    },
    UnreadPostResponses(Messages.Favorite_Name_PostResponses) {
        @Override
        protected IFavorite createFavorite(Integer id, String name, String config) {
            return new UnreadPostResponseFavorite(id, name, config);
        }
    },
    UnreadUserResponses(Messages.Favorite_Name_UserResponses) {
        @Override
        protected IFavorite createFavorite(Integer id, String name, String config) {
            return new UnreadUserResponseFavorite(id, name, config);
        }
    },
    Category(Messages.Favorite_Name_Category) {
        @Override
        protected IFavorite createFavorite(Integer id, String name, String config) {
            return new CategoryFavorite(id, name, config);
        }
    };

    private final Messages typeName;

    FavoriteType(Messages typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName.get();
    }

    protected abstract IFavorite createFavorite(Integer id, String name, String config);

    /**
     * Restores a favorite, basing on information
     *
     *
     * @param id
     * @param name
     * @param type
     * @param config
     *
     * @return
     *
     * @throws IllegalArgumentException if this favorite type is not exists
     */
    public static IFavorite restoreFavorite(int id, String name, String type, String config) throws IllegalArgumentException {
        return valueOf(type).createFavorite(id, name, config);
    }
}
