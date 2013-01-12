package org.xblackcat.rojac.gui.view.startpage;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.util.Comparator;

/**
 * @author xBlackCat
 */
class GroupItem<T extends AnItem> extends AGroupItem<T> {
    private final Message title;

    protected GroupItem(Message title) {
        this(title, null, null);
    }

    protected GroupItem(Message title, ReadStatusIcon iconSet) {
        this(title, null, iconSet);
    }

    protected GroupItem(Message title, Comparator<T> itemComparator) {
        this(title, itemComparator, null);
    }

    protected GroupItem(Message title, Comparator<T> itemComparator, ReadStatusIcon iconSet) {
        super(null, itemComparator, iconSet);
        this.title = title;
    }

    @Override
    JPopupMenu getContextMenu(IAppControl appControl) {
        return null;
    }

    @Override
    void onDoubleClick(IAppControl appControl) {
    }

    @Override
    String getBriefInfo() {
        return null;
    }

    @Override
    String getTitleLine() {
        return title.get();
    }

    @Override
    boolean isExuded() {
        for (AnItem child : children) {
            if (child.isExuded()) {
                return true;
            }
        }
        return false;
    }
}
