package org.xblackcat.rojac.gui.view.model;

import org.apache.commons.lang.ArrayUtils;
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
     *
     * @see #removeTreeModelListener
     */
    public void addTreeModelListener(TreeModelListener l) {
        modelSupport.addTreeModelListener(l);
    }

    /**
     * Removes a listener previously added with <B>addTreeModelListener()</B>.
     *
     * @param l the listener to remove
     *
     * @see #addTreeModelListener
     */
    public void removeTreeModelListener(TreeModelListener l) {
        modelSupport.removeTreeModelListener(l);
    }

    /**
     * Returns an array of all the tree model listeners registered on this model.
     *
     * @return all of this model's <code>TreeModelListener</code>s or an empty array if no tree model listeners are
     *         currently registered
     *
     * @see #addTreeModelListener
     * @see #removeTreeModelListener
     * @since 1.4
     */
    public TreeModelListener[] getTreeModelListeners() {
        return modelSupport.getTreeModelListeners();
    }

    public T getRoot() {
        return root;
    }

    public void setRoot(T root) {
        this.root = root;
        modelSupport.fireNewRoot();
    }

    /**
     * Invoke this method if you've modified the {@code MessageItem}s upon which this model depends. The model will
     * notify all of its listeners that the model has changed.
     */
    public void reload() {
        reload(root);
    }

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

    /**
     * Invoke this method if you've modified the {@code MessageItem}s upon which this model depends. The model will
     * notify all of its listeners that the model has changed below the given node.
     *
     * @param node the node below which the model has changed
     */
    public void reload(T node) {
        if (node != null) {
            modelSupport.firePathChanged(getPathToRoot(node));
        }
    }

    public void markInitialized() {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void nodeWasAdded(T node, T child) {
        if (node != null) {
            int cIndex = node.getIndex(child);

            if (cIndex != -1) {
                TreePath path = getPathToRoot(node);
                modelSupport.fireChildAdded(path, cIndex, child);
            }
        }
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
     * Builds the parents of node up to and including the root node, where the original node is the last element in the
     * returned array. The length of the returned array gives the node's depth in the tree.
     *
     * @param aNode the MessageItem to get the path for
     *
     * @return TreePath object with all subsequent nodes to reach the specified node from root.
     */
    public TreePath getPathToRoot(T aNode) {
        return new TreePath(getPathToRoot(aNode, 0));
    }

    protected abstract T[] getPathToRoot(T aNode, int depth);
}
