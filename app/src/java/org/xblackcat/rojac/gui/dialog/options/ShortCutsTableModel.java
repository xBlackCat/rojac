package org.xblackcat.rojac.gui.dialog.options;

import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.i18n.Messages;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author xBlackCat
 */
class ShortCutsTableModel extends AbstractTableModel {
    private final Map<ShortCut, KeyStroke> values = new EnumMap<ShortCut, KeyStroke>(ShortCut.class);

    public ShortCutsTableModel() {
        reset();
    }

    public void reset() {
        for (ShortCut sc : ShortCut.values()) {
            values.put(sc, sc.getKeyStroke());
        }

        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return ShortCut.values().length;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return Messages.class;
            default:
                return KeyStroke.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ShortCut shortCut = ShortCut.values()[rowIndex];

        switch (columnIndex) {
            case 0:
                return shortCut.name();
            case 1:
                return shortCut.getDescription();
            default:
                return values.get(shortCut);
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 2;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        ShortCut sc = ShortCut.values()[rowIndex];
        KeyStroke keyStroke = (KeyStroke) aValue;

        values.put(sc, keyStroke);

        fireTableRowsUpdated(sc.ordinal(), sc.ordinal());
    }

    public void commitChanges() {
        for (ShortCut sc : ShortCut.values()) {
            sc.setKeyStroke(values.get(sc));
        }
    }
}
