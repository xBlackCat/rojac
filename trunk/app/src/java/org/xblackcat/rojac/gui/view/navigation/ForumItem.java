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

    public ForumItem(AGroupItem<ForumItem> parent, Forum forum, ForumStatistic statistic) {
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
        int myReplies = statistic.getUnreadReplies();
        int unreadMessages = statistic.getUnreadMessages();
        int totalMessages = statistic.getTotalMessages();

        if (unreadMessages == 0) {
            return String.valueOf(totalMessages);
        } else if (unreadMessages == totalMessages) {
            if (myReplies == 0) {
                return String.valueOf(totalMessages);
            } else {
                return Message.View_Navigation_Item_ForumInfo_Full.get(
                        myReplies,
                        unreadMessages,
                        totalMessages
                );
            }
        } else {
            if (myReplies == 0) {
                return Message.View_Navigation_Item_ForumInfo.get(
                        unreadMessages,
                        totalMessages
                );
            } else {
                return Message.View_Navigation_Item_ForumInfo_Full.get(
                        myReplies,
                        unreadMessages,
                        totalMessages
                );
            }
        }
    }

    @Override
    String getTitleLine() {
        return forum.getForumName();
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