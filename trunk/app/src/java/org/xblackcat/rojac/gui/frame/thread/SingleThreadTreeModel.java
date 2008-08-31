package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.lang.NotImplementedException;

import javax.swing.tree.TreePath;

/**
 * Date: 22 бер 2008
 *
 * @author xBlackCat
 */
class SingleThreadTreeModel extends AThreadTreeModel {
    private MessageItem root;

    public void loadRoot(int itemId) {
        root = new MessageItem(null, itemId);
        fireTreeStructureChanged(this, getPathToRoot(root), null, null);
    }

    public MessageItem getRoot() {
        return root;
    }

    public MessageItem getChild(Object parent, int index) {
        return ((MessageItem) parent).getChildren()[index];
    }

    public int getChildCount(Object parent) {
        return ((MessageItem) parent).getChildren().length;
    }

    public boolean isLeaf(Object node) {
        return ((MessageItem) node).getChildren().length == 0;
    }

    public int getIndexOfChild(Object parent, Object child) {
        return ((MessageItem) parent).getIndex((MessageItem) child);
    }

    public void setRoot(MessageItem root) {
        this.root = root;
        reload();
    }

    /**
     * Invoke this method if you've modified the {@code MessageItem}s upon which this model depends. The model will
     * notify all of its listeners that the model has changed.
     */
    public void reload() {
        reload(root);
    }

    /**
     * This sets the user object of the MessageItem identified by path and posts a node changed.  If you use custom user
     * objects in the TreeModel you're going to need to subclass this and set the user object of the changed node to
     * something meaningful.
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new NotImplementedException();
    }

    /**
     * Invoke this method after you've changed how node is to be represented in the tree.
     */
    public void nodeChanged(MessageItem node) {
        if (listenerList != null && node != null) {
            MessageItem parent = node.getParent();

            if (parent != null) {
                int anIndex = parent.getIndex(node);
                if (anIndex != -1) {
                    int[] cIndexs = new int[1];

                    cIndexs[0] = anIndex;
                    nodesChanged(parent, cIndexs);
                }
            } else if (node == getRoot()) {
                nodesChanged(node, null);
            }
        }
    }

    /**
     * Invoke this method if you've modified the {@code MessageItem}s upon which this model depends. The model will
     * notify all of its listeners that the model has changed below the given node.
     *
     * @param node the node below which the model has changed
     */
    public void reload(MessageItem node) {
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * Invoke this method after you've inserted some TreeNodes into node.  childIndices should be the index of the new
     * elements and must be sorted in ascending order.
     */
    public void nodesWereInserted(MessageItem node, int[] childIndices) {
        if (listenerList != null && node != null && childIndices != null
                && childIndices.length > 0) {
            int cCount = childIndices.length;
            Object[] newChildren = new Object[cCount];

            MessageItem[] children = node.getChildren();
            for (int counter = 0; counter < cCount; counter++) {
                newChildren[counter] = children[childIndices[counter]];
            }
            fireTreeNodesInserted(this, getPathToRoot(node), childIndices,
                    newChildren);
        }
    }

    /**
     * Invoke this method after you've removed some TreeNodes from node.  childIndices should be the index of the
     * removed elements and must be sorted in ascending order. And removedChildren should be the array of the children
     * objects that were removed.
     */
    public void nodesWereRemoved(MessageItem node, int[] childIndices,
                                 Object[] removedChildren) {
        if (node != null && childIndices != null) {
            fireTreeNodesRemoved(this, getPathToRoot(node), childIndices,
                    removedChildren);
        }
    }

    /**
     * Invoke this method after you've changed how the children identified by childIndicies are to be represented in the
     * tree.
     */
    public void nodesChanged(MessageItem node, int[] childIndices) {
        if (node != null) {
            if (childIndices != null) {
                int cCount = childIndices.length;

                if (cCount > 0) {
                    Object[] cChildren = new Object[cCount];

                    MessageItem[] children = node.getChildren();
                    for (int counter = 0; counter < cCount; counter++) {
                        cChildren[counter] = children[childIndices[counter]];
                    }
                    fireTreeNodesChanged(this, getPathToRoot(node),
                            childIndices, cChildren);
                }
            } else if (node == getRoot()) {
                fireTreeNodesChanged(this, getPathToRoot(node), null, null);
            }
        }
    }

    /**
     * Invoke this method if you've totally changed the children of node and its childrens children...  This will post a
     * treeStructureChanged event.
     */
    public void nodeStructureChanged(MessageItem node) {
        if (node != null) {
            fireTreeStructureChanged(this, getPathToRoot(node), null, null);
        }
    }

    /**
     * Builds the parents of node up to and including the root node, where the original node is the last element in the
     * returned array. The length of the returned array gives the node's depth in the tree.
     *
     * @param aNode the MessageItem to get the path for
     */
    public MessageItem[] getPathToRoot(MessageItem aNode) {
        return getPathToRoot(aNode, 0);
    }

    /**
     * Builds the parents of node up to and including the root node, where the original node is the last element in the
     * returned array. The length of the returned array gives the node's depth in the tree.
     *
     * @param aNode the MessageItem to get the path for
     * @param depth an int giving the number of steps already taken towards the root (on recursive calls), used to size
     *              the returned array
     *
     * @return an array of TreeNodes giving the path from the root to the specified node
     */
    protected MessageItem[] getPathToRoot(MessageItem aNode, int depth) {
        MessageItem[] retNodes;
        // This method recurses, traversing towards the root in order
        // size the array. On the way back, it fills in the nodes,
        // starting from the root and working back to the original node.

        /* Check for null, in case someone passed in a null node, or
           they passed in an element that isn't rooted at root. */
        if (aNode == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = new MessageItem[depth];
            }
        } else {
            depth++;
            if (aNode == root) {
                retNodes = new MessageItem[depth];
            } else {
                retNodes = getPathToRoot(aNode.getParent(), depth);
            }
            retNodes[retNodes.length - depth] = aNode;
        }
        return retNodes;
    }
}
