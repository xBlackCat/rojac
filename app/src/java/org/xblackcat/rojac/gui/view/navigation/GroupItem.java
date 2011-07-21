package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.util.Comparator;

/**
 * @author xBlackCat
 */
public class GroupItem extends AGroupItem {
    private final Message title;

    protected GroupItem(Message title) {
        this(title, null);
    }

    protected GroupItem(Message title, Comparator<AnItem> itemComparator) {
        super(null, itemComparator);
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
        return String.valueOf(children.size());
    }

    @Override
    String getExtraInfo() {
        return null;
    }

    @Override
    String getTitleLine() {
        return title.get();
    }

    @Override
    String getExtraTitleLine() {
        return null;
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
