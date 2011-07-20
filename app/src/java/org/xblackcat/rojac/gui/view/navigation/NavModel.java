package org.xblackcat.rojac.gui.view.navigation;

import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.xblackcat.rojac.i18n.Message;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

/**
 * @author xBlackCat
 */
class NavModel implements TreeTableModel {
    private final TreeModelSupport support = new TreeModelSupport(this);
    private final ANavItem root;
    private final ANavItem personal;
    private final ANavItem favorites;

    final ForumDecorator forumDecorator;

    NavModel() {
        forumDecorator = new ForumDecorator(this, support);

        personal = new GroupNavItem(Message.View_Navigation_Item_Personal);
        favorites = new GroupNavItem(Message.View_Navigation_Item_Favorites);

        root = new RootNavItem(
                personal,
                forumDecorator.getSubscribedForums(),
                forumDecorator.getNotSubscribedForums(),
                favorites
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
        return ((ANavItem) parent).getChild(index);
    }

    @Override
    public int getChildCount(Object parent) {
        return ((ANavItem) parent).getChildCount();
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

    TreePath getPathToRoot(ANavItem aNode) {
        return new TreePath(getPathToRoot(aNode, 0));
    }

    ANavItem[] getPathToRoot(ANavItem aNode, int depth) {
        ANavItem[] retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if (aNode == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = new ANavItem[depth];
            }
        } else {
            depth++;
            if (aNode == root) {
                retNodes = new ANavItem[depth];
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
}
