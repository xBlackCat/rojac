package org.xblackcat.rojac.gui;

import bibliothek.gui.dock.dockable.AbstractDockable;

import javax.swing.*;

/**
 * @author xBlackCat
 */
class TitleChangeTracker implements IInfoChangeListener {
    private final IView itemView;
    private final AbstractDockable view;

    public TitleChangeTracker(IView itemView, AbstractDockable view) {
        this.itemView = itemView;
        this.view = view;
    }

    @Override
    public void infoChanged() {
        String title = itemView.getTabTitle();
        Icon icon = itemView.getTabTitleIcon();

        view.setTitleText(title);
        view.setTitleToolTip(title);
        view.setTitleIcon(icon);
    }
}
