package org.xblackcat.rojac.gui.model;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;

/**
 * Date: 21 вер 2008
 *
 * @author xBlackCat
 */

public class ForumData {
    private final Forum forum;
    private ForumStatistic stat;

    public ForumData(Forum forum) {
        this.forum = forum;
    }

    public Forum getForum() {
        return forum;
    }

    public ForumStatistic getStat() {
        return stat;
    }

    public void setStat(ForumStatistic stat) {
        this.stat = stat;
    }
}
