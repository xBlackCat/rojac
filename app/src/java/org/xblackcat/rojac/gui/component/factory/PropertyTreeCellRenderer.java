package org.xblackcat.rojac.gui.component.factory;

import org.xblackcat.rojac.gui.dialogs.PropertyNode;
import org.xblackcat.rojac.service.options.IValueChecker;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
* @author xBlackCat
*/
class PropertyTreeCellRenderer extends DefaultTreeCellRenderer {
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        PropertyNode pn = (PropertyNode) value;

        Property p = pn.getProperty();

        if (p != null) {
            IValueChecker checker = p.getChecker();
            String v = checker != null ? checker.getValueDescription(pn.getValue()) : String.valueOf(pn.getValue());
            setText(pn.getName() + " : " + v);
        } else {
            setText(pn.getName());
        }

        return this;
    }
}
