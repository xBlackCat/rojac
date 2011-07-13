package org.xblackcat.rojac.data;

import org.xblackcat.rojac.gui.view.forumlist.ForumData;

/**
 * @author xBlackCat
 */

public class ForumMessageData extends MessageData {
    private final ForumData forum;

    public ForumMessageData(Forum forum) {
        super(
                -1,
                -1,
                -1,
                forum.getForumId(),
                -1,
                forum.getForumName(),
                forum.getShortForumName(),
                -1,
                -1,
                true,
                null,
                false
        );
        this.forum = new ForumData(forum);
    }

    public ForumData getForum() {
        return forum;
    }
}
