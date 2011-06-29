package org.xblackcat.rojac.gui.theme;

import org.xblackcat.rojac.gui.view.model.ReadStatus;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author xBlackCat Date: 29.06.11
 */
public enum ReadStatusIcon {
    Post("message"),
    OwnPost("message-my"),
    HasResponse("message-has-response"),
    Response("message-response"),
    Favorite("favorite"),
    FavoriteThread("favorite-thread"),
    FavoritePostList("favorite-userposts"),
    FavoriteResponseList("favorite-userposts"),
    Thread("thread"),
    PostList("favorite-userposts"),
    ResponseList("favorite-userposts"),;

    private final Map<ReadStatus, AnIcon> iconsBuffer = new EnumMap<ReadStatus, AnIcon>(ReadStatus.class);

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
