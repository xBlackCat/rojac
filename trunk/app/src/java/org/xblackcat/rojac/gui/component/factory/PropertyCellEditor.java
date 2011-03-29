package org.xblackcat.rojac.gui.component.factory;

import org.xblackcat.rojac.gui.dialogs.PropertyNode;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeCellEditor;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.EventObject;

/**
 * @author xBlackCat
 */
public class PropertyCellEditor extends AbstractCellEditor implements TableCellEditor, TreeCellEditor {

//
//  Instance Variables
//

    /**
     * The Swing component being edited.
     */
    protected JComponent editorComponent;
    /**
     * The delegate class which handles all methods sent from the <code>CellEditor</code>.
     */
    protected EditorDelegate delegate;
    /**
     * An integer specifying the number of clicks needed to start editing. Even if <code>clickCountToStart</code> is
     * defined as zero, it will not initiate until a click occurs.
     */
    protected int clickCountToStart = 1;

//
//  Constructors
//

    /**
     * Constructs a <code>PropertyCellEditor</code> that uses a text field.
     *
     * @param component a <code>JTextField</code> object
     */
    public PropertyCellEditor(final AComplexEditor component) {
        editorComponent = component;
        this.clickCountToStart = 2;
        delegate = new EditorDelegate() {
            public void setValue(Object value) {
                component.setValue(value);
            }

            public Object getCellEditorValue() {
                return component.getValue();
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == AComplexEditor.ACTION_DONE) {
                    fireEditingStopped();
                } else if (e.getActionCommand() == AComplexEditor.ACTION_CANCEL) {
                    fireEditingCanceled();
                } else {
                    super.actionPerformed(e);
                }
            }
        };
        component.addActionListener(delegate);
    }
    /**
     * Constructs a <code>PropertyCellEditor</code> that uses a text field.
     *
     * @param textField a <code>JTextField</code> object
     */
    public PropertyCellEditor(final JTextField textField) {
        editorComponent = textField;
        this.clickCountToStart = 2;
        delegate = new EditorDelegate() {
            public void setValue(Object value) {
                textField.setText((value != null) ? value.toString() : "");
            }

            public Object getCellEditorValue() {
                return textField.getText();
            }
        };
        textField.addActionListener(delegate);
    }

    /**
     * Constructs a <code>PropertyCellEditor</code> object that uses a check box.
     *
     * @param checkBox a <code>JCheckBox</code> object
     */
    public PropertyCellEditor(final JCheckBox checkBox) {
        editorComponent = checkBox;
        delegate = new EditorDelegate() {
            public void setValue(Object value) {
                boolean selected = false;
                if (value instanceof Boolean) {
                    selected = (Boolean) value;
                } else if (value instanceof String) {
                    selected = value.equals("true");
                }
                checkBox.setSelected(selected);
            }

            public Object getCellEditorValue() {
                return checkBox.isSelected();
            }
        };
        checkBox.addActionListener(delegate);
        checkBox.setRequestFocusEnabled(false);
    }

    /**
     * Constructs a <code>PropertyCellEditor</code> object that uses a combo box.
     *
     * @param comboBox a <code>JComboBox</code> object
     */
    public PropertyCellEditor(final JComboBox comboBox) {
        editorComponent = comboBox;
        comboBox.putClientProperty("JComboBox.isTableCellEditor", Boolean.TRUE);
        delegate = new EditorDelegate() {
            public void setValue(Object value) {
                comboBox.setSelectedItem(value);
            }

            public Object getCellEditorValue() {
                return comboBox.getSelectedItem();
            }

            public boolean shouldSelectCell(EventObject anEvent) {
                if (anEvent instanceof MouseEvent) {
                    MouseEvent e = (MouseEvent) anEvent;
                    return e.getID() != MouseEvent.MOUSE_DRAGGED;
                }
                return true;
            }

            public boolean stopCellEditing() {
                if (comboBox.isEditable()) {
                    // Commit edited value.
                    comboBox.actionPerformed(new ActionEvent(
                            PropertyCellEditor.this, 0, ""));
                }
                return super.stopCellEditing();
            }
        };
        comboBox.addActionListener(delegate);
    }

    PropertyCellEditor(final JSpinner spinner) {
        editorComponent = spinner;

        delegate = new EditorDelegate() {
            public void setValue(Object value) {
                spinner.setValue(value);
            }

            public Object getCellEditorValue() {
                return value;
            }

            @Override
            public void stateChanged(ChangeEvent e) {
                this.value = spinner.getValue();
            }
        };
        spinner.addChangeListener(delegate);
    }

//
//  Implementing the TreeCellEditor Interface
//

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row) {
        if (value instanceof PropertyNode) {
            value = ((PropertyNode) value).getValue();
        } else {
            value = tree.convertValueToText(value, isSelected, expanded, leaf, row, false);
        }
        this.delegate.setValue(value);
        return this.editorComponent;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof PropertyNode) {
            value = ((PropertyNode) value).getValue();
        }
        this.delegate.setValue(value);
        if (this.editorComponent instanceof JCheckBox) {
            //in order to avoid a "flashing" effect when clicking a checkbox
            //in a table, it is important for the editor to have as a border
            //the same border that the renderer has, and have as the background
            //the same color as the renderer has. This is primarily only
            //needed for JCheckBox since this editor doesn't fill all the
            //visual space of the table cell, unlike a text field.
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component c = renderer.getTableCellRendererComponent(table, value,
                    isSelected, true, row, column);
            if (c != null) {
                this.editorComponent.setOpaque(true);
                this.editorComponent.setBackground(c.getBackground());
                if (c instanceof JComponent) {
                    this.editorComponent.setBorder(((JComponent) c).getBorder());
                }
            } else {
                this.editorComponent.setOpaque(false);
            }
        }
        return this.editorComponent;
    }


    /**
     * Returns a reference to the editor component.
     *
     * @return the editor <code>Component</code>
     */
    public Component getComponent() {
        return editorComponent;
    }

//
//  Modifying
//

    /**
     * Specifies the number of clicks needed to start editing.
     *
     * @param count an int specifying the number of clicks needed to start editing
     *
     * @see #getClickCountToStart
     */
    public void setClickCountToStart(int count) {
        clickCountToStart = count;
    }

    /**
     * Returns the number of clicks needed to start editing.
     *
     * @return the number of clicks needed to start editing
     */
    public int getClickCountToStart() {
        return clickCountToStart;
    }

//
//  Override the implementations of the superclass, forwarding all methods 
//  from the CellEditor interface to our delegate. 
//

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
     *
     * @see EditorDelegate#getCellEditorValue
     */
    public Object getCellEditorValue() {
        return delegate.getCellEditorValue();
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
     *
     * @see EditorDelegate#isCellEditable(EventObject)
     */
    public boolean isCellEditable(EventObject anEvent) {
        return delegate.isCellEditable(anEvent);
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
     *
     * @see EditorDelegate#shouldSelectCell(EventObject)
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        return delegate.shouldSelectCell(anEvent);
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
     *
     * @see EditorDelegate#stopCellEditing
     */
    public boolean stopCellEditing() {
        return delegate.stopCellEditing();
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
     *
     * @see EditorDelegate#cancelCellEditing
     */
    public void cancelCellEditing() {
        delegate.cancelCellEditing();
    }


    //
//  Protected EditorDelegate class
//

    /**
     * The protected <code>EditorDelegate</code> class.
     */
    protected class EditorDelegate implements ActionListener, ItemListener, Serializable, ChangeListener{

        /**
         * The value of this cell.
         */
        protected Object value;

        /**
         * Returns the value of this cell.
         *
         * @return the value of this cell
         */
        public Object getCellEditorValue() {
            return value;
        }

        /**
         * Sets the value of this cell.
         *
         * @param value the new value of this cell
         */
        public void setValue(Object value) {
            this.value = value;
        }

        /**
         * Returns true if <code>anEvent</code> is <b>not</b> a <code>MouseEvent</code>.  Otherwise, it returns true if
         * the necessary number of clicks have occurred, and returns false otherwise.
         *
         * @param anEvent the event
         *
         * @return true  if cell is ready for editing, false otherwise
         *
         * @see #setClickCountToStart
         * @see #shouldSelectCell
         */
        public boolean isCellEditable(EventObject anEvent) {
            return !(anEvent instanceof MouseEvent && ((MouseEvent) anEvent).getClickCount() < clickCountToStart);
        }

        /**
         * Returns true to indicate that the editing cell may be selected.
         *
         * @param anEvent the event
         *
         * @return true
         *
         * @see #isCellEditable
         */
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        /**
         * Returns true to indicate that editing has begun.
         *
         * @param anEvent the event
         */
        public boolean startCellEditing(EventObject anEvent) {
            return true;
        }

        /**
         * Stops editing and returns true to indicate that editing has stopped. This method calls
         * <code>fireEditingStopped</code>.
         *
         * @return true
         */
        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }

        /**
         * Cancels editing.  This method calls <code>fireEditingCanceled</code>.
         */
        public void cancelCellEditing() {
            fireEditingCanceled();
        }

        /**
         * When an action is performed, editing is ended.
         *
         * @param e the action event
         *
         * @see #stopCellEditing
         */
        public void actionPerformed(ActionEvent e) {
            PropertyCellEditor.this.stopCellEditing();
        }

        /**
         * When an item's state changes, editing is ended.
         *
         * @param e the action event
         *
         * @see #stopCellEditing
         */
        public void itemStateChanged(ItemEvent e) {
            PropertyCellEditor.this.stopCellEditing();
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            PropertyCellEditor.this.stopCellEditing();
        }

    }

} // End of class JCellEditor

