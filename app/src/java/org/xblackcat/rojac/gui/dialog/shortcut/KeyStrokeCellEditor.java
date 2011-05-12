package org.xblackcat.rojac.gui.dialog.shortcut;

import org.xblackcat.rojac.gui.component.AComplexEditor;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

/**
 * Created by IntelliJ IDEA. User: Alexey Date: 10.05.11 Time: 15:31 To change this template use File | Settings | File
 * Templates.
 */
class KeyStrokeCellEditor extends AbstractCellEditor implements TableCellEditor {
//
//  Instance Variables
//
    /**
     * The Swing component being edited.
     */
    protected KeyStrokeEditor editorComponent;
    /**
     * An integer specifying the number of clicks needed to start editing. Even if <code>clickCountToStart</code> is
     * defined as zero, it will not initiate until a click occurs.
     */
    protected int clickCountToStart = 2;

//
//  Constructors
//

    /**
     * Constructs a <code>DefaultCellEditor</code> that uses a text field.
     *
     * @param textField a <code>JTextField</code> object
     */
    public KeyStrokeCellEditor() {
        editorComponent = new KeyStrokeEditor();
        this.clickCountToStart = 2;

        editorComponent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand() == AComplexEditor.ACTION_CANCEL) {
                    cancelCellEditing();
                } else if (e.getActionCommand() == AComplexEditor.ACTION_DONE) {
                    stopCellEditing();
                }
            }
        });
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
        return editorComponent.getValue();
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
     *
     * @see EditorDelegate#isCellEditable(java.util.EventObject)
     */
    public boolean isCellEditable(EventObject anEvent) {
        return !(anEvent instanceof MouseEvent) || ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
     *
     * @see EditorDelegate#shouldSelectCell(java.util.EventObject)
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
     *
     * @see EditorDelegate#stopCellEditing
     */
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
     *
     * @see EditorDelegate#cancelCellEditing
     */
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

//
//  Implementing the TreeCellEditor Interface
//

//
//  Implementing the CellEditor Interface
//

    /**
     * Implements the <code>TableCellEditor</code> interface.
     */
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int row, int column) {
        if (value instanceof KeyStroke) {
            editorComponent.setValue((KeyStroke) value);
        } else {
            editorComponent.setValue(null);
        }
        return editorComponent;
    }


} // End of class JCellEditor
