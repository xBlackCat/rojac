package org.xblackcat.rojac.gui.view.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author xBlackCat Date: 21.07.11
 */
abstract class AGroupItem extends AnItem {
    protected final List<AnItem> children;
    protected final Comparator<AnItem> itemComparator;

    AGroupItem(AnItem parent, Comparator<AnItem> itemComparator) {
        this(parent, itemComparator, new ArrayList<AnItem>());
    }

    AGroupItem(AnItem parent, Comparator<AnItem> itemComparator, List<AnItem> children) {
        super(parent);
        this.itemComparator = itemComparator;
        this.children = children;
    }

    int add(AnItem item) {
        children.add(item);
        item.setParent(this);

        if (itemComparator != null) {
            Collections.sort(children, itemComparator);
            return children.indexOf(item);
        } else {
            return children.size();
        }
    }

    AnItem remove(int idx) {
        return children.remove(idx);
    }

    boolean isGroup() {
        return true;
    }

    @Override
    int getChildCount() {
        return children.size();
    }

    int indexOf(AnItem i) {
        assert children != null;

        return children.indexOf(i);
    }

    @Override
    AnItem getChild(int idx) {
        return children.get(idx);
    }
}
