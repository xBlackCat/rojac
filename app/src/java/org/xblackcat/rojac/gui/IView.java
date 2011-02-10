package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.gui.view.ViewId;
import org.xblackcat.rojac.service.datahandler.IDataHandler;

import javax.swing.*;

/**
 * Main interface of all views - components are aimed to show any kind of information.
 *
 * @author xBlackCat
 */

public interface IView extends IConfigurable, IDataHandler {
    /**
     * Returns a component represented the view.
     *
     * @return a component represented the view.
     */
    JComponent getComponent();

    ViewId getId();

    /**
     * Gets current state of the view.
     *
     * @return state object or <code>null</code> if view have no state to store.
     */
    IViewState getState();

    /**
     * Sets state of view. The state will be set after the view is fully initialized. If passed <code>null</code> a
     * default state will be used for the view.
     *
     * @param state correspond view
     */
    void setState(IViewState state);
}
