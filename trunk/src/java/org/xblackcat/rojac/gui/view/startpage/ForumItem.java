package org.xblackcat.rojac.gui.view.startpage;

import org.xblackcat.rojac.data.DiscussionStatistic;
import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.gui.view.model.ReadStatus;

import javax.swing.*;

/**
 * @author xBlackCat Date: 18.07.11
 */
class ForumItem extends AnItem {
    private final Forum forum;
    private DiscussionStatistic statistic;

    public ForumItem(AGroupItem<ForumItem> parent, Forum forum, DiscussionStatistic statistic) {
        super(parent);
        this.forum = forum;
        this.statistic = statistic;
    }

    @Override
    JPopupMenu getContextMenu(IAppControl appControl) {
        return PopupMenuBuilder.getForumViewMenu(forum, appControl);
    }

    @Override
    void onDoubleClick(IAppControl appControl) {
        appControl.openTab(ViewType.Forum.makeId(forum.getForumId()));
    }

    @Override
    Icon getIcon() {
        if (statistic == null) {
            return ReadStatusIcon.Forum.getIcon(ReadStatus.Read);
        }

        ReadStatusIcon statusIcon = statistic.getUnreadReplies() > 0 ? ReadStatusIcon.ForumWithReply : ReadStatusIcon.Forum;
        return statusIcon.getIcon(getReadStatus());
    }

    @Override
    ReadStatus getReadStatus() {
        if (statistic == null) {
            return ReadStatus.Read;
        }

        if (statistic.getUnreadMessages() == 0) {
            return ReadStatus.Read;
        }

        if (statistic.getTotalMessages() == statistic.getUnreadMessages()) {
            return ReadStatus.Unread;
        }

        return ReadStatus.ReadPartially;
    }

    @Override
    String getBriefInfo() {
        return statistic == null ? "---" : statistic.asString();
    }

    @Override
    String getTitleLine() {
        return forum.getForumName();
    }

    @Override
    boolean isExuded() {
        return statistic != null && statistic.getUnreadMessages() > 0;
    }

    Forum getForum() {
        return forum;
    }

    DiscussionStatistic getStatistic() {
        return statistic;
    }

    void setStatistic(DiscussionStatistic statistic) {
        this.statistic = statistic;
    }

    @Override
    boolean isGroup() {
        return false;
    }

    @Override
    int indexOf(AnItem i) {
        return -1;
    }

    @Override
    AnItem getChild(int idx) {
        return null;
    }

    @Override
    int getChildCount() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ForumItem)) {
            return false;
        }

        ForumItem that = (ForumItem) o;

        return forum.equals(that.forum);
    }

    @Override
    public int hashCode() {
        return forum.hashCode();
    }
}
