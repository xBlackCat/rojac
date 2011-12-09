package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang3.ArrayUtils;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.xblackcat.rojac.NotImplementedException;
import org.xblackcat.rojac.RojacException;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author xBlackCat
 */

public class SortedThreadsModel implements TreeModel, TreeTableModel {
    protected boolean initialized;
    protected Post root;
    /**
     * Provides support for event dispatching.
     */
    protected TreeModelSupport modelSupport = new TreeModelSupport(this);

    public Post getChild(Object parent, int index) {
        return ((Post) parent).getChild(index);
    }

    public int getChildCount(Object parent) {
        return ((Post) parent).getSize();
    }

    public boolean isLeaf(Object node) {
        return ((Post) node).isLeaf();
    }

    public int getIndexOfChild(Object parent, Object child) {
        return ((Post) parent).getIndex((Post) child);
    }

    /**
     * This sets the user object of the Post identified by path and posts a node changed.  If you use custom user
     * objects in the TreeModel you're going to need to subclass this and set the user object of the changed node to
     * something meaningful.
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new NotImplementedException();
    }

    public Class<?> getColumnClass(int columnIndex) {
        return Header.values()[columnIndex].getObjectClass();
    }

    public int getColumnCount() {
        return Header.values().length;
    }

    public String getColumnName(int column) {
        return Header.values()[column].getTitle();
    }

    public int getHierarchicalColumn() {
        return 1;
    }

    public Object getValueAt(Object node, int column) {
        try {
            return Header.values()[column].getObjectData(node);
        } catch (RojacException e) {
            throw new IllegalArgumentException("Can not convert node for column " + column + ". Node: " + node);
        }
    }

    public boolean isCellEditable(Object node, int column) {
        return false;
    }

    public void setValueAt(Object value, Object node, int column) {
        throw new NotImplementedException();
    }

    /**
     * Builds the parents of node up to and including the root node, where the original node is the last element in the
     * returned array. The length of the returned array gives the node's depth in the tree.
     *
     * @param aNode the Post to get the path for
     * @param depth an int giving the number of steps already taken towards the root (on recursive calls), used to size
     *              the returned array
     * @return an array of TreeNodes giving the path from the root to the specified node
     */
    protected Post[] getPathToRoot(Post aNode, int depth) {
        Post[] retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if (aNode == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = new Post[depth];
            }
        } else {
            depth++;
            if (aNode == root) {
                retNodes = new Post[depth];
            } else {
                retNodes = getPathToRoot(aNode.getParent(), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     *
     * @param l the listener to add
     * @see #removeTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void addTreeModelListener(TreeModelListener l) {
        modelSupport.addTreeModelListener(l);
    }

    /**
     * Removes a listener previously added with <B>addTreeModelListener()</B>.
     *
     * @param l the listener to remove
     * @see #addTreeModelListener(javax.swing.event.TreeModelListener)
     */
    public void removeTreeModelListener(TreeModelListener l) {
        modelSupport.removeTreeModelListener(l);
    }

    public Post getRoot() {
        return root;
    }

    public void setRoot(Post root) {
        this.root = root;
        modelSupport.fireNewRoot();
    }

    /**
     * Notifies model that all nodes in path to current node (inclusive) was changed.
     *
     * @param node node to identifya path.
     */
    public void pathToNodeChanged(Post node) {
        if (node != null) {
            TreePath toRoot = getPathToRoot(node);
            do {
                nodeChanged((Post) toRoot.getLastPathComponent());
                toRoot = toRoot.getParentPath();
            } while (toRoot != null);
        }
    }

    public void subTreeNodesChanged(Post node) {
        if (node != null) {
            subTreeChanged(getPathToRoot(node));
        }
    }

    private void subTreeChanged(TreePath path) {
        final Post node = (Post) path.getLastPathComponent();
        modelSupport.firePathChanged(path);

        for (int i = 0; i < node.getSize(); i++) {
            subTreeChanged(path.pathByAddingChild(node.getChild(i)));
        }
    }

    /**
     * Invoke this method after you've changed how node is to be represented in the tree.
     *
     * @param node changed node. Should belong the model
     */
    public void nodeChanged(Post node) {
        if (node != null) {
            Post parent = node.getParent();

            if (parent != null) {
                int anIndex = parent.getIndex(node);
                if (anIndex != -1) {
                    int[] cIndexes = new int[1];

                    cIndexes[0] = anIndex;
                    nodesChanged(parent, cIndexes);
                }
            } else if (node == getRoot()) {
                nodesChanged(node);
            }
        }
    }

    public void markInitialized() {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void nodeRemoved(Post parent, int childIdx, Post child) {
        modelSupport.fireChildRemoved(getPathToRoot(parent), childIdx, child);
    }

    /**
     * Invoke this method after you've changed how the children identified by childIndexes are to be represented in the
     * tree.
     *
     * @param node         root node of changed children
     * @param childIndexes list of changed child indexes
     */
    public void nodesChanged(Post node, int... childIndexes) {
        if (node != null) {
            if (!ArrayUtils.isEmpty(childIndexes)) {
                int cCount = childIndexes.length;

                if (cCount > 0) {
                    Object[] cChildren = new Object[cCount];

                    for (int counter = 0; counter < cCount; counter++) {
                        cChildren[counter] = node.getChild(childIndexes[counter]);
                    }
                    modelSupport.fireChildrenChanged(getPathToRoot(node), childIndexes, cChildren);
                }
            } else if (node == getRoot()) {
                modelSupport.firePathChanged(getPathToRoot(node));
            }
        }
    }

    /**
     * Invoke this method if you've totally changed the children of node and its children's children...  This will post
     * a treeStructureChanged event.
     *
     * @param node root node of changed sub-tree
     */
    public void nodeStructureChanged(Post node) {
        if (node != null) {
            modelSupport.fireTreeStructureChanged(getPathToRoot(node));
        }
    }

    /**
     * Syntetic method to indicate view to complete update data.
     */
    public void fireResortModel() {
        modelSupport.fireTreeStructureChanged(null);
    }

    /**
     * Builds the parents of node up to and including the root node, where the original node is the last element in the
     * returned array. The length of the returned array gives the node's depth in the tree.
     *
     * @param aNode the MessageItem to get the path for
     * @return TreePath object with all subsequent nodes to reach the specified node from root.
     */
    public TreePath getPathToRoot(Post aNode) {
        return new TreePath(getPathToRoot(aNode, 0));
    }

    public boolean isPathValid(TreePath treePath) {
        if (treePath == null || treePath.getPathCount() == 0) {
            return false;
        }

        Object[] path = treePath.getPath();
        Post root = getRoot();

        if (!path[0].equals(root)) {
            return false;
        }

        Post parent = root;

        for (int i = 1, path1Length = path.length; i < path1Length; i++) {
            Post p = (Post) path[i];

            if (parent.getIndex(p) < 0) {
                return false;
            }

            parent = p;
        }

        return true;
    }
}
