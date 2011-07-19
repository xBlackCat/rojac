package org.xblackcat.rojac.gui.view.navigation;

import javax.swing.table.DefaultTableCellRenderer;

/**
 * @author xBlackCat Date: 19.07.11
 */
public class InfoCellRenderer extends DefaultTableCellRenderer {
    @Override
    protected void setValue(Object value) {
        super.setValue(((ANavItem) value).getBriefInfo());
    }
}
