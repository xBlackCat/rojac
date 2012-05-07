package org.xblackcat.rojac.gui.view.thread;

import org.jdesktop.swingx.JXTreeTable;
import org.xblackcat.rojac.gui.view.model.Post;
import org.xblackcat.rojac.gui.view.model.SortedThreadsModel;
import org.xblackcat.rojac.gui.view.model.ViewMode;

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

        ViewMode mode = ((SortedThreadsModel) treeTable.getTreeTableModel()).getMode();

        return getTableCellRendererComponent(treeTable, new TreeCellPostProxy(mode == ViewMode.Compact, post), sel, hasFocus, row, treeTable.getHierarchicalColumn());
    }
}
