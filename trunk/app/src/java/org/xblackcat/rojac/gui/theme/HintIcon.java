package org.xblackcat.rojac.gui.theme;

import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Definition of available icons to be used in link preview hints.
 *
 * @author xBlackCat
 */

public enum HintIcon implements Icon {
    Info("hint-info.png"),
    ThreadChanged("hint-thread-changed.png"),
    // --------
    ;

    private final IResourceIcon icon;

    private HintIcon(String filename) {
        icon = new SimpleIcon(filename);
    }

    private Icon getDelegatedIcon() {
        return UIUtils.getIcon(icon);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        getDelegatedIcon().paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
        return getDelegatedIcon().getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return getDelegatedIcon().getIconHeight();
    }
}
