package org.xblackcat.rojac.gui.dialog.options;

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

    public KeyStrokeCellEditor() {
        editorComponent = new KeyStrokeEditor();
        this.clickCountToStart = 2;

        editorComponent.addActionListener(new ActionListener() {
            @SuppressWarnings({"StringEquality"})
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

//
//  Override the implementations of the superclass, forwarding all methods
//  from the CellEditor interface to our delegate.
//

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
          */
    public Object getCellEditorValue() {
        return editorComponent.getValue();
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
     */
    public boolean isCellEditable(EventObject anEvent) {
        return !(anEvent instanceof MouseEvent) || ((MouseEvent) anEvent).getClickCount() >= clickCountToStart;
    }

    /**
     * Forwards the message from the <code>CellEditor</code> to the <code>delegate</code>.
     */
    public boolean shouldSelectCell(EventObject anEvent) {
        return true;
    }

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
