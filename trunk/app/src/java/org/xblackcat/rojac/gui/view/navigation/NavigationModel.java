package org.xblackcat.rojac.gui.view.navigation;

import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

/**
 * @author xBlackCat
 */
class NavigationModel extends AModelControl implements TreeTableModel {
    private final TreeModelSupport support = new TreeModelSupport(this);
    private final AnItem root;
    private final AnItem personal;
    private final AnItem favorites;

    final ForumDecorator forumDecorator;

    NavigationModel() {
        forumDecorator = new ForumDecorator(this);

        personal = new GroupItem(Message.View_Navigation_Item_Personal);
        favorites = new GroupItem(Message.View_Navigation_Item_Favorites);

        root = new RootItem(
                personal,
                forumDecorator.getSubscribedForums(),
                forumDecorator.getNotSubscribedForums(),
                favorites
        );
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return AnItem.class;
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
    public AnItem getValueAt(Object node, int column) {
        return (AnItem) node;
    }

    @Override
    public boolean isCellEditable(Object node, int column) {
        return false;
    }

    @Override
    public void setValueAt(Object value, Object node, int column) {
    }

    @Override
    public AnItem getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        return ((AnItem) parent).getChild(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((AnItem) parent).getChildCount();
    }

    @Override
    public boolean isLeaf(Object node) {
        assert node instanceof AnItem;
        return !((AnItem) node).isGroup();
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        if (parent == null || child == null) {
            return -1;
        }

        assert parent instanceof AnItem;
        assert child instanceof AnItem;

        AnItem c = (AnItem) child;
        AnItem p = (AnItem) parent;

        if (c.getParent() != parent) {
            // Only strict equality!!
            return -1;
        }

        if (!p.isGroup()) {
            return -1;
        }

        // Not null!
        return p.indexOf(c);
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
        support.addTreeModelListener(l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
        support.removeTreeModelListener(l);
    }

    ForumDecorator getForumDecorator() {
        return forumDecorator;
    }

    TreePath getPathToRoot(AnItem aNode) {
        return new TreePath(getPathToRoot(aNode, 0));
    }

    AnItem[] getPathToRoot(AnItem aNode, int depth) {
        AnItem[] retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if (aNode == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = new AnItem[depth];
            }
        } else {
            depth++;
            if (aNode == root) {
                retNodes = new AnItem[depth];
            } else {
                retNodes = getPathToRoot(aNode.getParent(), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    public void load() {
        getForumDecorator().reloadForums();
    }

    // Helper methods
    @Override
    void safeRemoveChild(AGroupItem parent, AnItem forum) {
        int idx = parent.indexOf(forum);
        if (idx != -1) {
            AnItem removed = parent.remove(idx);
            support.fireChildRemoved(getPathToRoot(parent), idx, removed);
        }
    }

    /**
     * Add a child to item and notify listeners about this.
     *
     * @param parent parent item to add child to
     * @param child a new child item
     */
    @Override
    void addChild(AGroupItem parent, AnItem child) {
        int idx = parent.add(child);

        support.fireChildAdded(getPathToRoot(parent), idx, child);
    }

    /**
     * Notify listeners that item was updated (and whole path to it too)
     *
     * @param item updated item
     */
    @Override
    void itemUpdated(AnItem item) {
        TreePath path = getPathToRoot(item);
        while (path != null) {
            support.firePathChanged(path);
            path = path.getParentPath();
        }
    }
}
