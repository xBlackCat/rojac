package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.util.ShortCutUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class KeyStrokeCellRenderer extends DefaultTableCellRenderer {
    @Override
    protected void setValue(Object value) {
        String text = ShortCutUtils.keyStrokeHint((KeyStroke) value);
        setText(text);
        setToolTipText(text);
    }
}
