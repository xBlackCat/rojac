package org.xblackcat.rojac.gui.view.startpage;

import org.jdesktop.swingx.JXTreeTable;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat Date: 19.07.11
 */
class LabelCellRenderer extends InfoCellRenderer implements TreeCellRenderer {
    private final JXTreeTable treeTable;

    public LabelCellRenderer(JXTreeTable treeTable) {
        this.treeTable = treeTable;
        setOpaque(false);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        return getTableCellRendererComponent(treeTable, value, sel, hasFocus, row, treeTable.getHierarchicalColumn());
    }

    @Override
    protected void setInfo(AnItem v) {
        setIcon(v.getIcon());
        setText(v.getTitleLine());
        setToolTipText(v.getTitleLine());
    }
}
