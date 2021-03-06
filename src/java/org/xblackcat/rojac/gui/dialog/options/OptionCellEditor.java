package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.i18n.NodeText;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Proxy class to choose proper editor
 */
class OptionCellEditor extends AbstractCellEditor implements TreeCellEditor {
    private PropertyCellEditor delegatedEditor = null;

    private final JPanel container = new JPanel(new BorderLayout());
    private final JPanel editor = new JPanel(new BorderLayout());
    private final JLabel label = new JLabel();

    OptionCellEditor() {
        container.add(label, BorderLayout.WEST);
        container.add(editor, BorderLayout.CENTER);
    }

    @Override
    public Object getCellEditorValue() {
        return delegatedEditor.getCellEditorValue();
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        if (event == null) {
            return true;
        }
        PropertyNode<?> propertyNode = getNode(event);

        if (propertyNode != null) {
            delegatedEditor = ComponentFactory.createTreeCellEditor(propertyNode);

            return delegatedEditor != null && delegatedEditor.isCellEditable(event);
        } else {
            return false;
        }
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        PropertyNode<?> n = (PropertyNode) value;

        delegatedEditor = ComponentFactory.createTreeCellEditor(n);

        // Copy all listeners into a new cell editor
        for (CellEditorListener l : getCellEditorListeners()) {
            delegatedEditor.addCellEditorListener(l);
        }

        editor.removeAll();
        editor.add(delegatedEditor.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row));

        label.setText(NodeText.Name.get(n) + " : ");
        label.setToolTipText(NodeText.Tip.get(n));

        container.invalidate();
        container.repaint();

        return container;
    }

    @Override
    public boolean stopCellEditing() {
        if (delegatedEditor == null) {
            return true;
        } else if (delegatedEditor.stopCellEditing()) {
            delegatedEditor.getCellEditorValue();

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void cancelCellEditing() {
        if (delegatedEditor != null) {
            delegatedEditor.stopCellEditing();
        }
    }

    private static PropertyNode getNode(EventObject event) {
        PropertyNode propertyNode = null;
        if (event != null) {
            if (event.getSource() instanceof JTree) {
                JTree tree = (JTree) event.getSource();
                if (event instanceof MouseEvent) {
                    TreePath path = tree.getPathForLocation(
                            ((MouseEvent) event).getX(),
                            ((MouseEvent) event).getY());

                    propertyNode = (PropertyNode) path.getLastPathComponent();

                }
            }
        }
        return propertyNode;
    }
}
