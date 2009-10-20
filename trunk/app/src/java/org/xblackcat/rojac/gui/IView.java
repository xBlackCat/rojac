package org.xblackcat.rojac.gui;

import javax.swing.*;

/**
 * @author xBlackCat
 */

public interface IView extends IConfigurable {
    JComponent getComponent();

    void updateData(int... ids);
}
