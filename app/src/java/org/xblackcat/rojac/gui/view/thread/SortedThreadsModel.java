package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.data.Mark;
import org.xblackcat.rojac.service.ServiceFactory;
import org.xblackcat.rojac.service.storage.IStorage;
import org.xblackcat.rojac.util.MessageUtils;
import org.xblackcat.rojac.util.RojacWorker;

import javax.swing.tree.TreePath;
import java.util.List;

/**
 * @author xBlackCat
 */

class SortedThreadsModel extends AThreadModel<Post> {
    public Post getChild(Object parent, int index) {
        final Post child = ((Post) parent).getChild(index);
        if (child.getRating() == null) {
            child.setRating(MessageUtils.NO_MARKS);

            new MarksLoader(child).execute();
        }
        return child;
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
     *
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

    private class MarksLoader extends RojacWorker<Void, Mark> {
        private final Post child;

        public MarksLoader(Post child) {
            this.child = child;
        }

        @Override
        protected Void perform() throws Exception {
            int messageId = child.getMessageId();
            IStorage storage = ServiceFactory.getInstance().getStorage();

            publish(storage.getRatingAH().getRatingMarksByMessageId(messageId));
            publish(storage.getNewRatingAH().getNewRatingMarksByMessageId(messageId));

            return null;
        }

        @Override
        protected void process(List<Mark> chunks) {
            child.setRating(chunks.toArray(new Mark[chunks.size()]));

            nodeChanged(child);
        }
    }
}
