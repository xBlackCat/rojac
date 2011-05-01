package org.xblackcat.rojac.gui;

import net.infonode.docking.View;
import net.infonode.docking.properties.ViewProperties;

import javax.swing.*;

/**
* @author xBlackCat
*/
class TitleChangeTracker implements IActionListener {
    private final IView itemView;
    private final View view;

    public TitleChangeTracker(IView itemView, View view) {
        this.itemView = itemView;
        this.view = view;
    }

    @Override
    public void itemGotFocus(Integer forumId, Integer messageId) {
    }

    @Override
    public void itemLostFocus(Integer forumId, Integer messageId) {
    }

    @Override
    public void itemUpdated(Integer forumId, Integer messageId) {
        String title = itemView.getTabTitle();
        Icon icon = itemView.getTabTitleIcon();

        final ViewProperties viewProperties = view.getViewProperties();

        viewProperties.setTitle(title);
        viewProperties.setIcon(icon);
        view.getWindowProperties().getTabProperties().getTitledTabProperties().getNormalProperties().setToolTipText(title);
    }
}
