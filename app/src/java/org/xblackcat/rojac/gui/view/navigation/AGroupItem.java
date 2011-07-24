package org.xblackcat.rojac.gui.view.navigation;

import org.xblackcat.rojac.gui.theme.ReadStatusIcon;
import org.xblackcat.rojac.gui.view.model.ReadStatus;
import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author xBlackCat Date: 21.07.11
 */
abstract class AGroupItem<T extends AnItem> extends AnItem {
    protected final List<T> children;
    protected final Comparator<T> itemComparator;
    protected final ReadStatusIcon iconSet;

    AGroupItem(AnItem parent, Comparator<T> itemComparator) {
        this(parent, itemComparator, null, null);
    }

    AGroupItem(AnItem parent, Comparator<T> itemComparator, ReadStatusIcon iconSet) {
        this(parent, itemComparator, new ArrayList<T>(), iconSet);
    }

    AGroupItem(AnItem parent, Comparator<T> itemComparator, List<T> children) {
        this(parent, itemComparator, children, null);
    }

    private AGroupItem(AnItem parent, Comparator<T> itemComparator, List<T> children, ReadStatusIcon iconSet) {
        super(parent);
        this.itemComparator = itemComparator;
        this.children = children;
        this.iconSet = iconSet;
    }

    int add(T item) {
        children.add(item);
        item.setParent(this);

        if (itemComparator != null) {
            Collections.sort(children, itemComparator);
            return children.indexOf(item);
        } else {
            return children.size() - 1;
        }
    }

    T remove(int idx) {
        return children.remove(idx);
    }

    void clear() {
        children.clear();
    }

    @Override
    Icon getIcon() {
        ReadStatus status = getReadStatus();
        if (iconSet == null) {
            return null;
        }

        return UIUtils.getIcon(iconSet.getIcon(status));
    }

    @Override
    ReadStatus getReadStatus() {
        if (children.isEmpty()) {
            return ReadStatus.Read;
        }

        boolean hasUnread = false;
        boolean hasRead = false;

        for (T item : children) {
            ReadStatus s = item.getReadStatus();
            if (s == ReadStatus.Read) {
                if (hasUnread) {
                    return ReadStatus.ReadPartially;
                } else {
                    hasRead = true;
                }
            } else if (s == ReadStatus.Unread) {
                if (hasRead) {
                    return ReadStatus.ReadPartially;
                } else {
                    hasUnread = true;
                }
            }
        }

        if (hasRead == hasUnread) {
            return ReadStatus.ReadPartially;
        }

        // Now only one of flag is set
        return hasRead ? ReadStatus.Read : ReadStatus.Unread;
    }

    boolean isGroup() {
        return true;
    }

    @Override
    int getChildCount() {
        return children.size();
    }

    <V extends AnItem> int indexOf(V i) {
        assert children != null;

        return children.indexOf(i);
    }

    @Override
    T getChild(int idx) {
        return children.get(idx);
    }
}
