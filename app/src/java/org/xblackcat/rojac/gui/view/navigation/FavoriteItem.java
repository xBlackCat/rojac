package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.data.Favorite;
import org.xblackcat.rojac.data.FavoriteStatData;
import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.popup.PopupMenuBuilder;
import org.xblackcat.rojac.gui.view.ViewType;

import javax.swing.*;

/**
 * @author xBlackCat Date: 18.07.11
 */
class FavoriteItem extends AnItem {
    private Favorite favorite;
    private FavoriteStatData statistic;

    public FavoriteItem(AnItem parent, Favorite favorite) {
        super(parent);
        this.favorite = favorite;
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
    String getBriefInfo() {
        if (statistic != null) {
            return statistic.asString();
        } else {
            return "...";
        }
    }

    @Override
    String getExtraInfo() {
        return null;
    }

    @Override
    String getTitleLine() {
        return favorite.getName();
    }

    @Override
    String getExtraTitleLine() {
        return null;
    }

    @Override
    boolean isExuded() {
        return statistic != null && statistic.getUnread() > 0;
    }

    Favorite getFavorite() {
        return favorite;
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
        if (this == o) return true;
        if (!(o instanceof FavoriteItem)) return false;

        FavoriteItem that = (FavoriteItem) o;

        return favorite.equals(that.favorite);
    }

    @Override
    public int hashCode() {
        return favorite.hashCode();
    }
}
