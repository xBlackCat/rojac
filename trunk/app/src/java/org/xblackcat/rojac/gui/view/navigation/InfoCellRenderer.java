package org.xblackcat.rojac.gui.view.navigation;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat Date: 19.07.11
 */
class InfoCellRenderer extends DefaultTableCellRenderer {
    @Override
    protected void setValue(Object value) {
        AnItem v = (AnItem) value;
        setInfo(v);

        Font f = getFont();
        if (f != null) {
            setFont(f.deriveFont(v.isExuded() ? Font.BOLD : Font.PLAIN));
        }
    }

    protected void setInfo(AnItem v) {
        setHorizontalAlignment(RIGHT);
        String text = v.getBriefInfo();
        setText(text);
        setToolTipText(text);
    }
}
