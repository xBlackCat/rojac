package org.xblackcat.rojac.gui.view.navigation;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat Date: 19.07.11
 */
class InfoCellRenderer extends DefaultTableCellRenderer {
    @Override
    protected void setValue(Object value) {
        setHorizontalAlignment(RIGHT);
        AnItem v = (AnItem) value;
        String text = v.getBriefInfo();
        setText(text);
        setToolTipText(text);

        Font f = getFont();
        if (f != null) {
            setFont(f.deriveFont(v.isExuded() ? Font.BOLD : Font.PLAIN));
        }
    }
}
