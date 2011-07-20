package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.gui.view.forumlist.ForumData;
import org.xblackcat.rojac.i18n.Message;

/**
 * @author xBlackCat Date: 18.07.11
 */
public class ForumNavItem extends ANavItem {
    private final Forum forum;
    private ForumStatistic statistic;

    public ForumNavItem(ANavItem parent, ForumData fd) {
        super(parent);
        this.forum = fd.getForum();
        this.statistic = fd.getStat();
    }

    @Override
    String getBriefInfo() {
        return Message.View_Navigation_Item_ForumInfo.get(
                statistic.getUnreadMessages(),
                statistic.getTotalMessages()
        );

    }

    @Override
    String getExtraInfo() {
        return null;
    }

    @Override
    String getTitleLine() {
        return forum.getForumName();
    }

    @Override
    String getExtraTitleLine() {
        return forum.getShortForumName();
    }

    Forum getForum() {
        return forum;
    }

    ForumStatistic getStatistic() {
        return statistic;
    }

    void setStatistic(ForumStatistic statistic) {
        this.statistic = statistic;
    }

    @Override
    boolean isGroup() {
        return false;
    }

    @Override
    int indexOf(ANavItem i) {
        return -1;
    }

    @Override
    ANavItem getChild(int idx) {
        return null;
    }

    @Override
    int getChildCount() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForumNavItem)) return false;

        ForumNavItem that = (ForumNavItem) o;

        return forum.equals(that.forum);
    }

    @Override
    public int hashCode() {
        return forum.hashCode();
    }
}
