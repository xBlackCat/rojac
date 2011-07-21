package org.xblackcat.rojac.gui.view.navigation;

import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author xBlackCat Date: 19.07.11
 */
public class InfoCellRenderer extends DefaultTableCellRenderer {
    @Override
    protected void setValue(Object value) {
        ANavItem v = (ANavItem) value;
        String text = v.getBriefInfo();
        setText(text);
        setToolTipText(text);

        Font f = getFont();
        if (f != null) {
            setFont(f.deriveFont(v.isExuded() ? Font.BOLD : Font.PLAIN));
        }
    }
}
