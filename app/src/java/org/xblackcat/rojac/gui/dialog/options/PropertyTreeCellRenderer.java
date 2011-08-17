package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.gui.component.ComplexTreeRenderer;
import org.xblackcat.rojac.gui.component.LineRenderer;
import org.xblackcat.rojac.gui.theme.OptionsIcon;
import org.xblackcat.rojac.i18n.NodeText;
import org.xblackcat.rojac.service.options.IValueChecker;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;
import java.awt.*;

/**
 * @author xBlackCat
 */
class PropertyTreeCellRenderer extends ComplexTreeRenderer {
    private final LineRenderer name = new LineRenderer(SwingConstants.LEFT);
    private final LineRenderer value = new LineRenderer(SwingConstants.LEFT);
    private final LineRenderer separator = new LineRenderer(":", SwingConstants.LEFT);

    PropertyTreeCellRenderer() {
        super(new FlowLayout(FlowLayout.LEFT, 3, 0));

        setEnabled(true);
        add(name);
        add(separator);
        add(value);

        setDelegatedComponents(name, separator, value);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        PropertyNode pn = (PropertyNode) value;

        Property p = pn.getProperty();

        name.setText(NodeText.Name.get(pn));

        setToolTipText(NodeText.Tip.get(pn));

        this.value.setIcon(null);
        if (p != null) {
            IValueChecker checker = p.getChecker();
            String v;
            if (checker != null) {
                v = checker.getValueDescription(pn.getValue());
            } else if (pn.getValue() instanceof Boolean) {
                v = null;
            } else {
                v = String.valueOf(pn.getValue());
            }
            separator.setVisible(true);
            this.value.setText(v);

            if (checker != null) {
                this.value.setIcon(checker.getValueIcon(pn.getValue()));
            } else if (pn.getValue() instanceof Boolean) {
                Boolean b = (Boolean) pn.getValue();
                if (b != null) {
                    this.value.setIcon(b ? OptionsIcon.Enabled : OptionsIcon.Disabled);
                }
            }
        } else {
            this.value.setText(null);
            separator.setVisible(false);
        }

        return this;
    }
}
