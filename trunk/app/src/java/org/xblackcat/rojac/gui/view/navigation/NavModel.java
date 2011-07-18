package org.xblackcat.rojac.gui.view.navigation;

import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import java.util.Arrays;

/**
 * @author xBlackCat
 */
class NavModel implements TreeTableModel {
    private final TreeModelSupport support = new TreeModelSupport(this);
    private final ANavItem root;
    private final ANavItem outbox;
    private final ANavItem subscribedForums;
    private final ANavItem notSubscribedForums;
    private final ANavItem favorites;
    private final ANavItem ignored;

    NavModel() {
        root = new RootNavItem();

        outbox = new GroupNavItem(root, Message.View_Navigation_Outbox);
        subscribedForums = new GroupNavItem(root, Message.View_Navigation_SubscribedForums);
        notSubscribedForums = new GroupNavItem(root, Message.View_Navigation_NotSubscribedForums);
        favorites = new GroupNavItem(root, Message.View_Navigation_Favorites);
        ignored = new GroupNavItem(root, Message.View_Navigation_Ignored);

        root.setChildren(Arrays.asList(
                outbox,
                subscribedForums,
                notSubscribedForums,
                favorites,
                ignored)
        );
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return ANavItem.class;
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int column) {
        return null;
    }

    @Override
    public int getHierarchicalColumn() {
        return 0;
    }

    @Override
    public ANavItem getValueAt(Object node, int column) {
        return (ANavItem) node;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return false;
    }

    @Override
    public void setValueAt(Object value, Object node, int column) {
    }

    @Override
    public ANavItem getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((ANavItem) parent).children.get(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((ANavItem) parent).children.size();
    }

    @Override
    public boolean isLeaf(Object node) {
        assert node instanceof ANavItem;
        return !((ANavItem) node).isGroup();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) {
            return -1;
        }

        assert parent instanceof ANavItem;
        assert child instanceof ANavItem;

        ANavItem c = (ANavItem) child;
        ANavItem p = (ANavItem) parent;

        if (c.getParent() != parent) {
            // Only strict comparation!!
            return -1;
        }

        if (!p.isGroup()) {
            return -1;
        }

        // Not null!
        return p.children.indexOf(c);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        support.addTreeModelListener(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        support.removeTreeModelListener(l);
    }
}
