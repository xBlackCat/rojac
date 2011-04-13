package org.xblackcat.rojac.gui.view.model;

import gnu.trove.TIntObjectHashMap;
import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.data.IFavorite;
import org.xblackcat.rojac.data.MessageData;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Root node for
 *
 * @author xBlackCat
 */

public class FavoritePostList extends Post {
    private static final MessageData FAKE = new MessageData(-1, -1, -1, -1, -1, "", "", -1, -1, true, null);

    private TIntObjectHashMap<Post> listPosts = new TIntObjectHashMap<Post>();
    private final static Comparator<Post> SORT_BY_DATE = new Comparator<Post>() {
        @Override
        public int compare(Post o1, Post o2) {
            long thisVal = o2.getMessageData().getMessageDate();
            long anotherVal = o1.getMessageData().getMessageDate();
            return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
        }
    };

    // State fields
    /**
     * State flag to indicate if this thread is filled with posts or not. If the thread is filled - statistic will be
     * calculated from real posts data. A stat data from DB is user in other case.
     */
    private LoadingState loadingState = LoadingState.NotLoaded;

    private final IFavorite favorite;

    public FavoritePostList(IFavorite favorite) {
        super(FAKE, null, null);
        this.favorite = favorite;
    }

    @Override
    public long getLastPostDate() {
        throw new NotImplementedException("The method shouldn't be invoked");
    }

    @Override
    public int compareTo(Post o) {
        throw new NotImplementedException("The method shouldn't be invoked");
    }

    public void setLoadingState(LoadingState loadingState) {
        this.loadingState = loadingState;
    }

    @Override
    public LoadingState getLoadingState() {
        return loadingState;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public Post getMessageById(int messageId) {
        return listPosts.get(messageId);
    }

    /**
     * Returns associated favorite with the list.
     *
     * @return associated favorite.
     */
    public IFavorite getFavorite() {
        return favorite;
    }

    void fillList(Iterable<MessageData> posts) {
        for (MessageData post : posts) {
            Post p = listPosts.get(post.getMessageId());

            if (p == null) {
                // New post
                Post newPost = new Post(post, this, null);

                listPosts.put(post.getMessageId(), newPost);

                // Postpone sorting
                childrenPosts.add(newPost);
            } else {
                // Post exists

                p.setMessageData(post);
            }
        }

        // Sorting...
        Collections.sort(childrenPosts, SORT_BY_DATE);
    }
}
