package org.xblackcat.rojac.gui.dialog.subscribtion;

import org.xblackcat.rojac.data.Forum;

/**
* @author xBlackCat
*/
class ForumInfo {
    private final Forum forum;
    private boolean subscribed;

    ForumInfo(Forum forum) {
        this.forum = forum;
        this.subscribed = forum.isSubscribed();
    }

    /**
     * Returns new subscribtion status and null if status is not changed.
     *
     * @return new subscribtion status.
     */
    Boolean getSubscribeStatus() {
        return (subscribed == forum.isSubscribed()) ? null : subscribed;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public Forum getForum() {
        return forum;
    }
}
