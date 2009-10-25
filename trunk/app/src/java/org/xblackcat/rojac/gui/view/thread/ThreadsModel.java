package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.lang.NotImplementedException;
import org.jdesktop.swingx.tree.TreeModelSupport;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * @author xBlackCat
 */

public class ThreadsModel implements TreeModel, TreeTableModel {
    private static final Messages[] COLUMN_NAMES = new Messages[]{
            Messages.PANEL_THREAD_HEADER_ID,
            Messages.PANEL_THREAD_HEADER_SUBJECT,
            Messages.PANEL_THREAD_HEADER_USER,
            Messages.PANEL_THREAD_HEADER_RATING,
            Messages.PANEL_THREAD_HEADER_DATE
    };

    private MessageItem root;
    /**
     * Provides support for event dispatching.
     */
    protected TreeModelSupport modelSupport = new TreeModelSupport(this);

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

    /**
     * This sets the user object of the MessageItem identified by path and posts a node changed.  If you use custom user
     * objects in the TreeModel you're going to need to subclass this and set the user object of the changed node to
     * something meaningful.
     */
    public void valueForPathChanged(TreePath path, Object newValue) {
        throw new NotImplementedException();
    }

    public Class<?> getColumnClass(int columnIndex) {
        return MessageItem.class;
    }

    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    public String getColumnName(int column) {
        return COLUMN_NAMES[column].get();
    }

    public int getHierarchicalColumn() {
        return 1;
    }

    public Object getValueAt(Object node, int column) {
        return (MessageItem) node;
    }

    public boolean isCellEditable(Object node, int column) {
        return false;
    }

    public void setValueAt(Object value, Object node, int column) {
        throw new NotImplementedException();
    }

    public void setRoot(MessageItem root) {
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

    /**
     * Invoke this method after you've changed how node is to be represented in the tree.
     */
    public void nodeChanged(MessageItem node) {
        if (node != null) {
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
            modelSupport.firePathChanged(getPathToRoot(node));
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
                    modelSupport.fireChildrenChanged(getPathToRoot(node), childIndices, cChildren);
                }
            } else if (node == getRoot()) {
                modelSupport.firePathChanged(getPathToRoot(node));
            }
        }
    }

    /**
     * Invoke this method if you've totally changed the children of node and its childrens children...  This will post a
     * treeStructureChanged event.
     */
    public void nodeStructureChanged(MessageItem node) {
        if (node != null) {
            modelSupport.fireTreeStructureChanged(getPathToRoot(node));
        }
    }

    /**
     * Builds the parents of node up to and including the root node, where the original node is the last element in the
     * returned array. The length of the returned array gives the node's depth in the tree.
     *
     * @param aNode the MessageItem to get the path for
     */
    public TreePath getPathToRoot(MessageItem aNode) {
        return new TreePath(getPathToRoot(aNode, 0));
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
