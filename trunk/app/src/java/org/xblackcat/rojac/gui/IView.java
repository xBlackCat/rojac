package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.service.datahandler.IDataHandler;

import javax.swing.*;

/**
 * Main interface of all views - components are aimed to show any kind of information.
 *
 * @author xBlackCat
 */

public interface IView extends IDataHandler, IStatefull {
    /**
     * Returns a component represented the view.
     *
     * @return a component represented the view.
     */
    JComponent getComponent();

    ViewId getId();

    void addStateChangeListener(IStateListener l);

    void removeStateChangeListener(IStateListener l);

    /**
     * Returns an object with current layout informaion.
     *
     * @return layout config object.
     */
    IViewLayout storeLayout();

    /**
     * Restores the view layout by data stored in the layout object.
     *
     * @param o layout config object.
     */
    void setupLayout(IViewLayout o);

    /**
     * Returns a title of the item tab depending on its state.
     *
     * @return title of a tab
     */
    String getTabTitle();

    /**
     * Returns an icon to be shown in view title (usually - in tab title)
     *
     * @return an icon or <code>null</code> if no icon is provided.
     */
    Icon getTabTitleIcon();

    /**
     * Returns a popup menu for view title.
     *
     * @return popup menu or <code>null</code> if no popup is provided.
     */
    JPopupMenu getTabTitleMenu();

    void addInfoChangeListener(IInfoChangeListener l);

    void removeInfoChangeListener(IInfoChangeListener l);
}
