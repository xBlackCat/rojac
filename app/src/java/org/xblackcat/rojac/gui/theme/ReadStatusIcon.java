package org.xblackcat.rojac.gui.theme;

import org.xblackcat.rojac.gui.view.model.ReadStatus;

import java.util.EnumMap;
import java.util.Map;

/**
 * Icon factory.
 * <p/>
 * Date: 29.06.11
 *
 * @author xBlackCat
 */
public enum ReadStatusIcon {
    /**
     * Single post read status icons. Common case.
     */
    Post("message"),
    /**
     * Single post read status icons. Own posts case.
     */
    OwnPost("message-my"),
    /**
     * Single post read status icon. Own posts with response case.
     */
    HasResponse("message-has-response"),
    /**
     * Single post read status icon. Reply on own post case.
     */
    Response("message-response"),
    /**
     * Common favorite read status icons.
     */
    Favorite("favorite"),
    /**
     * Favorite thread read status icons.
     */
    FavoriteThread("favorite-thread"),
    /**
     * Favorite user's post list read status icons.
     */
    FavoritePostList("favorite-userposts"),
    /**
     * Reply list on favorite user's posts read status icons.
     */
    FavoriteResponseList("favorite-userreplies"),
    /**
     * Thread read status icons.
     */
    Thread("thread"),
    /**
     * Thread with unread replies read status icons. Note: for now it contains only "unread" icon and shouldn't be used
     * with others statues.
     */
    ThreadHasResponse("thread-has-response"),
    /**
     * User's post list read status icons.
     */
    PostList("user-post"),
    /**
     * Reply list on user's posts read status icons.
     */
    ReplyList("user-reply"),
    ;

    private final Map<ReadStatus, AnIcon> iconsBuffer = new EnumMap<>(ReadStatus.class);

    ReadStatusIcon(String base) {
        iconsBuffer.put(ReadStatus.Read, new SimpleIcon(base + "-read.png"));
        iconsBuffer.put(ReadStatus.ReadPartially, new SimpleIcon(base + "-read-partially.png"));
        iconsBuffer.put(ReadStatus.Unread, new SimpleIcon(base + "-unread.png"));
    }

    public AnIcon getIcon(ReadStatus status) {
        assert status != null;
        return iconsBuffer.get(status);
    }
}
