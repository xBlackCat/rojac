package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.gui.IAppControl;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author xBlackCat
 */
public class GroupNavItem extends ANavItem {
    private final Message title;
    private List<ANavItem> children = new ArrayList<ANavItem>();

    protected GroupNavItem(Message title) {
        super(null);
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
        for (ANavItem child : children) {
            if (child.isExuded()) {
                return true;
            }
        }
        return false;
    }

    boolean isGroup() {
        return true;
    }

    @Override
    int getChildCount() {
        return children.size();
    }

    int indexOf(ANavItem i) {
        assert children != null;

        return children.indexOf(i);
    }

    @Override
    ANavItem getChild(int idx) {
        return children.get(idx);
    }

    int add(ANavItem item, Comparator<ANavItem> comp) {
        children.add(item);
        item.setParent(this);

        if (comp != null) {
            Collections.sort(children, comp);
            return children.indexOf(item);
        } else {
            return children.size();
        }
    }

    ANavItem remove(int idx) {
        return children.remove(idx);
    }
}
