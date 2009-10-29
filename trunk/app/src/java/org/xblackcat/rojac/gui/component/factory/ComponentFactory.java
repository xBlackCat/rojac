package org.xblackcat.rojac.gui.component.factory;

import org.apache.commons.lang.ArrayUtils;
import org.xblackcat.rojac.gui.dialogs.PropertyNode;
import org.xblackcat.rojac.service.options.IValueChecker;
import org.xblackcat.rojac.service.options.Password;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xBlackCat
 */

public final class ComponentFactory {
    private static final Map<Class, ListCellRenderer> LIST_RENDERERS = new HashMap<Class, ListCellRenderer>();
    private static final Map<Class, TreeCellRenderer> TREE_RENDERERS = new HashMap<Class, TreeCellRenderer>();
    private static final DefaultTreeCellRenderer DEFAULT_TREE_CELL_RENDERER = new DefaultTreeCellRenderer();
    private static final PropertyTreeCellRenderer PROPERTY_TREE_CELL_RENDERER = new PropertyTreeCellRenderer();
    private static final int TEXT_FIELD_WIDTH = 10;

    public static PropertyCellEditor createTreeCellEditor(PropertyNode propertyNode) {
        if (propertyNode == null) {
            return null;
        }

        Property p = propertyNode.getProperty();
        if (p == null) {
            return null;
        }

        IValueChecker valueChecker = p.getChecker();

        Object currentValue = propertyNode.getValue();

        if (valueChecker != null) {
            if (!ArrayUtils.isEmpty(valueChecker.getPossibleValues())) {
                // Return combo box based editor
                ComboBoxModel model = new ComboBoxEditorModel(
                        valueChecker,
                        currentValue
                );

                JComboBox combo = new JComboBox(model);
                combo.setRenderer(new PropertyListCellRenderer(valueChecker));

                return new PropertyCellEditor(combo);
            }
        }

        // Formatted cell editor
        if (p.getType() == Boolean.class) {
            JCheckBox checkBox = new JCheckBox();
            checkBox.setSelected(((Boolean) currentValue).booleanValue());
            return new PropertyCellEditor(checkBox);
        }

        if (p.getType() == Integer.class ||
                p.getType() == Long.class ||
                p.getType() == Byte.class ||
                p.getType() == Short.class) {
            NumberFormat format = NumberFormat.getIntegerInstance();
            JTextField field = new JFormattedTextField(format);

            field.setColumns(TEXT_FIELD_WIDTH);
            field.setText(String.valueOf(currentValue));

//            JSpinner field = new JSpinner(new SpinnerNumberModel());

            return new PropertyCellEditor(field);
        }

        if (p.getType() == Double.class ||
                p.getType() == Float.class) {
            NumberFormat format = NumberFormat.getNumberInstance();
            JTextField field = new JFormattedTextField(format);

            field.setColumns(TEXT_FIELD_WIDTH);
            field.setText(String.valueOf(currentValue));

            return new PropertyCellEditor(field);
        }

        if (p.getType() == Password.class) {
            Password pass = (Password) currentValue;
            JPasswordField field = new JPasswordField();

            field.setColumns(TEXT_FIELD_WIDTH);
            field.setText(String.valueOf(pass.getPassword()));

            return new PropertyCellEditor(field);
        }

        JTextField field = new JTextField(String.valueOf(currentValue));
        field.setColumns(TEXT_FIELD_WIDTH);

        return new PropertyCellEditor(field);
    }

    public static TreeCellRenderer createTreeCellRenderer(PropertyNode pn) {
        return PROPERTY_TREE_CELL_RENDERER;
/*
        Property p = pn.getProperty();

        if (p == null) {
            return DEFAULT_TREE_CELL_RENDERER;
        }

        if (TREE_RENDERERS.containsKey(p.getType())) {
            return TREE_RENDERERS.get(p.getType());
        }

        TreeCellRenderer renderer = PROPERTY_TREE_CELL_RENDERER;

        TREE_RENDERERS.put(p.getType(), renderer);

        return renderer;
*/
    }

    public static ListCellRenderer createListCellRenderer(Property p) {
        if (LIST_RENDERERS.containsKey(p.getType())) {
            return LIST_RENDERERS.get(p.getType());
        }

        ListCellRenderer renderer;
        if (p.getChecker() != null) {
            renderer = new PropertyListCellRenderer(p.getChecker());
        } else {
            // TODO: generate renderer by property type 
            renderer = new DefaultListCellRenderer();
        }

        LIST_RENDERERS.put(p.getType(), renderer);

        return renderer;
    }

}
