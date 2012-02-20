package org.xblackcat.rojac.gui.view.forumlist;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;

/**
 * @author xBlackCat
 */

public class ForumData {
    private final int forumId;
    private Forum forum;
    private ForumStatistic stat;

    public ForumData(Forum forum) {
        this(forum, ForumStatistic.noStatistic(forum.getForumId()));
    }

    public ForumData(Forum forum, ForumStatistic statistic) {
        this.forum = forum;
        forumId = forum.getForumId();
        stat = statistic;
    }

    public Forum getForum() {
        return forum;
    }

    public int getForumId() {
        return forumId;
    }

    public ForumStatistic getStat() {
        return stat;
    }

    public void setStat(ForumStatistic stat) {
        this.stat = stat;
    }

    public void setSubscribed(boolean subscribed) {
        this.forum = forum.setSubscribed(subscribed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ForumData forumData = (ForumData) o;

        return forumId == forumData.forumId;

    }

    @Override
    public int hashCode() {
        return forumId;
    }
}
