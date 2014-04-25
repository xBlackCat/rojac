package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.service.datahandler.IDataHandler;

import javax.swing.*;

/**
 * Main interface of all views - components are aimed to show any kind of information.
 *
 * @author xBlackCat
 */

public interface IView extends IDataHandler, ILayoutful {
    /**
     * Returns a component represented the view.
     *
     * @return a component represented the view.
     */
    JComponent getComponent();

    /**
     * Returns a title of the item tab depending on its state.
     *
     * @return title of a tab
     */
    String getTabTitle();

    /**
     * Returns an icon to be shown in view title (usually - in tab title)
     *
     * @return an icon or {@code null} if no icon is provided.
     */
    Icon getTabTitleIcon();

    /**
     * Returns a popup menu for view title.
     *
     * @return popup menu or {@code null} if no popup is provided.
     */
    JPopupMenu getTabTitleMenu();

}
