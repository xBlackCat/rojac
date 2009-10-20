package org.xblackcat.rojac.gui.frame.thread;

import org.apache.commons.lang.NotImplementedException;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author xBlackCat
 */

public abstract class AThreadTreeModel implements TreeModel {
    protected MessageItem root;

    public abstract void loadRoot(int itemId);

    /**
     * Listeners.
     */
    protected EventListenerList listenerList = new EventListenerList();

    //
    //  Events
    //

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     *
     * @param l the listener to add
     *
     * @see #removeTreeModelListener
     */
    public void addTreeModelListener(TreeModelListener l) {
        listenerList.add(TreeModelListener.class, l);
    }

    /**
     * Removes a listener previously added with <B>addTreeModelListener()</B>.
     *
     * @param l the listener to remove
     *
     * @see #addTreeModelListener
     */
    public void removeTreeModelListener(TreeModelListener l) {
        listenerList.remove(TreeModelListener.class, l);
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
        return listenerList.getListeners(TreeModelListener.class);
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type.  The event instance is
     * lazily created using the parameters passed into the fire method.
     *
     * @param source       the node being changed
     * @param path         the path to the root node
     * @param childIndices the indices of the changed elements
     * @param children     the changed elements
     *
     * @see EventListenerList
     */
    protected void fireTreeNodesChanged(Object source, Object[] path,
                                        int[] childIndices,
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type.  The event instance is
     * lazily created using the parameters passed into the fire method.
     *
     * @param source       the node where new elements are being inserted
     * @param path         the path to the root node
     * @param childIndices the indices of the new elements
     * @param children     the new elements
     *
     * @see EventListenerList
     */
    protected void fireTreeNodesInserted(Object source, Object[] path,
                                         int[] childIndices,
                                         Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type.  The event instance is
     * lazily created using the parameters passed into the fire method.
     *
     * @param source       the node where elements are being removed
     * @param path         the path to the root node
     * @param childIndices the indices of the removed elements
     * @param children     the removed elements
     *
     * @see EventListenerList
     */
    protected void fireTreeNodesRemoved(Object source, Object[] path,
                                        int[] childIndices,
                                        Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type.  The event instance is
     * lazily created using the parameters passed into the fire method.
     *
     * @param source       the node where the tree model has changed
     * @param path         the path to the root node
     * @param childIndices the indices of the affected elements
     * @param children     the affected elements
     *
     * @see EventListenerList
     */
    protected void fireTreeStructureChanged(Object source, Object[] path,
                                            int[] childIndices,
                                            Object[] children) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path,
                            childIndices, children);
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type.  The event instance is
     * lazily created using the parameters passed into the fire method.
     *
     * @param source the node where the tree model has changed
     * @param path   the path to the root node
     *
     * @see EventListenerList
     */
    private void fireTreeStructureChanged(Object source, TreePath path) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        TreeModelEvent e = null;
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TreeModelListener.class) {
                // Lazily create the event:
                if (e == null) {
                    e = new TreeModelEvent(source, path);
                }
                ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
            }
        }
    }

    public MessageItem getRoot() {
        return root;
    }

    public MessageItem getChild(Object parent, int index) {
        return ((MessageItem) parent).getChildren(this)[index];
    }

    public int getChildCount(Object parent) {
        return ((MessageItem) parent).getChildren(this).length;
    }

    public boolean isLeaf(Object node) {
        return ((MessageItem) node).getChildren(this).length == 0;
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

            MessageItem[] children = node.getChildren(this);
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

                    MessageItem[] children = node.getChildren(this);
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
