package org.xblackcat.rojac.gui.model;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;

/**
 * Date: 21 вер 2008
 *
 * @author xBlackCat
 */

public class ForumData {
    private final int forumId;
    private Forum forum;
    private ForumStatistic stat;

    public ForumData(int forumId) {
        this.forumId = forumId;
    }

    public ForumData(Forum forum) {
        this.forum = forum;
        forumId = forum.getForumId();
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForumData forumData = (ForumData) o;

        return forumId == forumData.forumId;

    }

    @Override
    public int hashCode() {
        return forumId;
    }
}
