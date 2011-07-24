package org.xblackcat.rojac.gui.view.navigation;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat Date: 19.07.11
 */
class LabelCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        AnItem v = (AnItem) value;
        setIcon(v.getIcon());
        setText(v.getTitleLine());
        setToolTipText(v.getTitleLine());
        Font f = getFont();
        if (f == null) {
            f = tree.getFont();
        }

        if (f != null) {
            setFont(f.deriveFont(v.isExuded() ? Font.BOLD : Font.PLAIN));
        }

        return this;
    }
}
