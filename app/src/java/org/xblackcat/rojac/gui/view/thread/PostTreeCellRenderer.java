package org.xblackcat.rojac.gui.view.thread;

import org.jdesktop.swingx.JXTreeTable;
import org.xblackcat.rojac.gui.view.model.Post;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat
 */
class PostTreeCellRenderer extends PostTableCellRenderer implements TreeCellRenderer {
    private final JXTreeTable treeTable;

    PostTreeCellRenderer(JXTreeTable treeTable) {
        this.treeTable = treeTable;
        setOpaque(false);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Post post = (Post) value;

        return getTableCellRendererComponent(treeTable, new TreeCellPostProxy(post), sel, hasFocus, row, treeTable.getHierarchicalColumn());
    }
}
