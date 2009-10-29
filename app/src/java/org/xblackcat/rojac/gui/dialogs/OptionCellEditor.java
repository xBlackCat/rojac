package org.xblackcat.rojac.gui.dialogs;

import org.xblackcat.rojac.gui.component.factory.ComponentFactory;
import org.xblackcat.rojac.gui.component.factory.PropertyCellEditor;

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
    protected CellEditorListener[] listeners;

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
        PropertyNode propertyNode = getNode(event);

        if (propertyNode != null) {
            delegatedEditor = ComponentFactory.createTreeCellEditor(propertyNode);

            return delegatedEditor != null && delegatedEditor.isCellEditable(event);
        } else {
            return false;
        }
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        PropertyNode n = (PropertyNode) value;

        delegatedEditor = ComponentFactory.createTreeCellEditor(n);

        // Copy all listeners into a new cell editor
        for (CellEditorListener l : getCellEditorListeners()) {
            delegatedEditor.addCellEditorListener(l);
        }

        editor.removeAll();
        editor.add(delegatedEditor.getTreeCellEditorComponent(tree, value, isSelected, expanded, leaf, row));

        JLabel renderer = (JLabel) ComponentFactory.createTreeCellRenderer(n).getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, true);
        label.setText(n.getName() + " : ");
        label.setIcon(renderer.getIcon());

        container.invalidate();
        container.repaint();
        
        return container;
    }

    @Override
    public boolean stopCellEditing() {
        if (delegatedEditor == null || delegatedEditor.stopCellEditing()) {
            Object value = delegatedEditor.getCellEditorValue();

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
