package org.xblackcat.rojac.gui;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.WindowPopupMenuFactory;

import javax.swing.*;

/**
 * @author xBlackCat
 */
class ItemViewPopupFactory implements WindowPopupMenuFactory {
    private final IView itemView;

    public ItemViewPopupFactory(IView itemView) {
        this.itemView = itemView;
    }

    @Override
    public JPopupMenu createPopupMenu(DockingWindow window) {
        return itemView.getTabTitleMenu();
    }
}
