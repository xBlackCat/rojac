package org.xblackcat.sunaj.gui.view;

import org.xblackcat.sunaj.gui.IConfigurable;

import javax.swing.*;

/**
 * Date: 15 лип 2008
 *
 * @author xBlackCat
 */

public interface IView extends IConfigurable {
    JComponent getComponent();
}
