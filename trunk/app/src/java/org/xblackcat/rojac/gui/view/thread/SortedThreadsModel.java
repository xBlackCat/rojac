package org.xblackcat.rojac.gui.view.thread;

import org.apache.commons.lang.NotImplementedException;
import org.xblackcat.rojac.RojacException;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.tree.TreePath;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author xBlackCat
 */

class SortedThreadsModel extends AThreadModel<Post> {
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

    private static enum Header {
        ID(PostId.class, Messages.PANEL_THREAD_HEADER_ID),
        SUBJECT(Post.class, Messages.PANEL_THREAD_HEADER_SUBJECT),
        USER(PostUser.class, Messages.PANEL_THREAD_HEADER_USER),
        DATE(PostDate.class, Messages.PANEL_THREAD_HEADER_DATE);

        private final Class<?> aClass;
        private final Constructor<?> constructor;
        private final Messages title;

        Header(Class<?> aClass, Messages title) {
            this.aClass = aClass;
            this.title = title;
            if (aClass.getSuperclass() == APostData.class) {
                try {
                    this.constructor = aClass.getConstructor(Post.class);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Can not prepare convertor for class " + aClass.getName(), e);
                }
            } else {
                this.constructor = null;
            }
        }

        public Class<?> getObjectClass() {
            return aClass;
        }

        public Object getObjectData(Object o) throws RojacException {
            if (constructor != null && o instanceof Post) {
                try {
                    return constructor.newInstance(o);
                } catch (InstantiationException e) {
                    throw new RojacException("Can not initialize data class " + aClass.getName(), e);
                } catch (IllegalAccessException e) {
                    throw new RojacException("Security check not passed for data class " + aClass.getName(), e);
                } catch (InvocationTargetException e) {
                    throw new RojacException("Can not initialize data class " + aClass.getName(), e);
                }
            } else {
                return o;
            }
        }

        public String getTitle() {
            return title.get();
        }
    }
}
