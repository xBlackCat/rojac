package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang3.ArrayUtils;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.TreeTableModel;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author xBlackCat
 */

@SuppressWarnings({"unchecked"})
public abstract class AThreadModel<T extends ITreeItem<T>> implements TreeModel, TreeTableModel {
    protected boolean initialized;
    protected T root;
    /**
     * Provides support for event dispatching.
     */
    protected TreeModelSupport modelSupport = new TreeModelSupport(this);

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

    public T getRoot() {
        return root;
    }

    public void setRoot(T root) {
        this.root = root;
        modelSupport.fireNewRoot();
    }

    /**
     * Notifies model that all nodes in path to current node (inclusive) was changed.
     *
     * @param node node to identifya path.
     */
    public void pathToNodeChanged(T node) {
        if (node != null) {
            TreePath toRoot = getPathToRoot(node);
            do {
                nodeChanged((T) toRoot.getLastPathComponent());
                toRoot = toRoot.getParentPath();
            } while (toRoot != null);
        }
    }

    public void subTreeNodesChanged(T node) {
        if (node != null) {
            subTreeChanged(getPathToRoot(node));
        }
    }

    private void subTreeChanged(TreePath path) {
        final T node = (T) path.getLastPathComponent();
        nodeChanged(node);

        for (int i = 0; i < node.getSize(); i++) {
            subTreeChanged(path.pathByAddingChild(node.getChild(i)));
        }
    }

    /**
     * Invoke this method after you've changed how node is to be represented in the tree.
     *
     * @param node changed node. Should belong the model
     */
    public void nodeChanged(T node) {
        if (node != null) {
            T parent = node.getParent();

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

    public void nodeRemoved(T parent, int childIdx, T child) {
        modelSupport.fireChildRemoved(getPathToRoot(parent), childIdx, child);
    }

    /**
     * Invoke this method after you've changed how the children identified by childIndexes are to be represented in the
     * tree.
     *
     * @param node         root node of changed children
     * @param childIndexes list of changed child indexes
     */
    public void nodesChanged(T node, int... childIndexes) {
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
    public void nodeStructureChanged(T node) {
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
    public TreePath getPathToRoot(T aNode) {
        return new TreePath(getPathToRoot(aNode, 0));
    }

    protected abstract T[] getPathToRoot(T aNode, int depth);

    public boolean isPathValid(TreePath treePath) {
        if (treePath == null || treePath.getPathCount() == 0) {
            return false;
        }

        Object[] path = treePath.getPath();
        T root = getRoot();

        if (!path[0].equals(root)) {
            return false;
        }

        T parent = root;

        for (int i = 1, path1Length = path.length; i < path1Length; i++) {
            T p = (T) path[i];

            if (parent.getIndex(p) < 0) {
                return false;
            }

            parent = p;
        }

        return true;
    }
}
