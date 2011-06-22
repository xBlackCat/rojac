package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.i18n.Message;

import javax.swing.table.DefaultTableCellRenderer;

public class MessagesCellRenderer extends DefaultTableCellRenderer {
    @Override
    protected void setValue(Object value) {
        String text = ((Message) value).get();
        setText(text);
        setToolTipText(text);
    }
}
