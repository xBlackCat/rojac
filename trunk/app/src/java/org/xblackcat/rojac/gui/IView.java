package org.xblackcat.rojac.gui;

import org.xblackcat.rojac.service.IDataHandler;

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
}
