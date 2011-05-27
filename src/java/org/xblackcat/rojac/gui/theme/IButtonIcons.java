package org.xblackcat.rojac.gui.theme;

import javax.swing.*;

/**
 * Helper interface to obtain icons for button.
 *
 * @author xBlackCat
 */

public interface IButtonIcons {
    Icon getDefaultIcon();

    Icon getDisabledIcon();

    Icon getRolloverIcon();

    Icon getRolloverSelectedIcon();

    Icon getSelectedIcon();
}
