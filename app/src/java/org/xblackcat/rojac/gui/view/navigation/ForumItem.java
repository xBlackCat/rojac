package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.data.Forum;
import org.xblackcat.rojac.data.ForumStatistic;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.gui.view.model.ReadStatus;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;

/**
 * @author xBlackCat Date: 18.07.11
 */
class ForumItem extends AnItem {
    private final Forum forum;
    private ForumStatistic statistic;

    public ForumItem(AnItem parent, Forum forum, ForumStatistic statistic) {
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
        return ReadStatusIcon.Thread.getIcon(getReadStatus());
    }

    @Override
    ReadStatus getReadStatus() {
        if (statistic == null) {
            return ReadStatus.Read;
        }

        if (statistic.getTotalMessages() == statistic.getUnreadMessages()) {
            return ReadStatus.Unread;
        }

        if (statistic.getUnreadMessages() == 0) {
            return ReadStatus.Read;
        }

        return ReadStatus.ReadPartially;
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

    @Override
    boolean isExuded() {
        return statistic.getUnreadMessages() > 0;
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
