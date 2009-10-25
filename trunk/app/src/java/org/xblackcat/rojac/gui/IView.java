package org.xblackcat.rojac.gui;

import javax.swing.*;

/**
 * Main interface of all views - components are aimed to show any kind of information.
 *
 * @author xBlackCat
 */

public interface IView extends IConfigurable {
    /**
     * Returns a component represented the view.
     *
     * @return a component represented the view.
     */
    JComponent getComponent();

    /**
     * Updates items data identified by theirs ids.
     *
     * @param ids id of items to be updated.
     */
    void updateData(int... ids);
}
