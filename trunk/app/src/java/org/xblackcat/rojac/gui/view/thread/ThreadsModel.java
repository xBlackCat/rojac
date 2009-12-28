package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.tree.TreePath;

/**
 * @author xBlackCat
 */

class ThreadsModel extends AThreadModel<MessageItem> {
    private static final Messages[] COLUMN_NAMES = new Messages[]{
            Messages.PANEL_THREAD_HEADER_ID,
            Messages.PANEL_THREAD_HEADER_SUBJECT,
            Messages.PANEL_THREAD_HEADER_USER,
            Messages.PANEL_THREAD_HEADER_RATING,
            Messages.PANEL_THREAD_HEADER_DATE
    };

    //
    //  Events
    //

    public ITreeItem getChild(Object parent, int index) {
        return ((ITreeItem) parent).getChild(index);
    }

    public int getChildCount(Object parent) {
        return ((ITreeItem) parent).getSize();
    }

    public boolean isLeaf(Object node) {
        ITreeItem item = (ITreeItem) node;
        switch (item.getLoadingState()) {
            case Loading:
                return true;
            case Loaded:
                return item.getSize() == 0;
            case NotLoaded:
            default:
                return false;
        }
    }

    public int getIndexOfChild(Object parent, Object child) {
        return ((ITreeItem) parent).getIndex((ITreeItem) child);
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
        return (ITreeItem) node;
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