package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.data.ReadStatistic;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.view.ViewType;
import org.xblackcat.rojac.gui.view.model.ReadStatus;

import javax.swing.*;

/**
 * @author xBlackCat Date: 18.07.11
 */
class FavoriteItem extends AnItem {
    private Favorite favorite;
    private ReadStatistic statistic;

    public FavoriteItem(Favorite favorite, ReadStatistic statistic) {
        super(null);
        this.favorite = favorite;
        this.statistic = statistic;
    }

    @Override
    JPopupMenu getContextMenu(IAppControl appControl) {
        return PopupMenuBuilder.getFavoritesMenu(favorite, appControl);
    }

    @Override
    void onDoubleClick(IAppControl appControl) {
        appControl.openTab(ViewType.Favorite.makeId(favorite.getId()));
    }

    @Override
    Icon getIcon() {
        return favorite.getType().getIcons().getIcon(getReadStatus());
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
        if (statistic != null) {
            return statistic.asString();
        } else {
            return "...";
        }
    }

    @Override
    String getTitleLine() {
        return favorite.getName();
    }

    @Override
    boolean isExuded() {
        return statistic != null && statistic.getUnreadMessages() > 0;
    }

    Favorite getFavorite() {
        return favorite;
    }

    void setStatistic(ReadStatistic statistic) {
        this.statistic = statistic;
    }

    ReadStatistic getStatistic() {
        return statistic;
    }

    public void setName(String newName) {
        favorite = favorite.setName(newName);
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
        if (!(o instanceof FavoriteItem)) {
            return false;
        }

        FavoriteItem that = (FavoriteItem) o;

        return favorite.equals(that.favorite);
    }

    @Override
    public int hashCode() {
        return favorite.hashCode();
    }
}
