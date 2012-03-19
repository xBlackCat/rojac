package org.xblackcat.rojac.gui.theme;

import org.xblackcat.rojac.util.UIUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Definition of available icons to be used in thread view for posts.
 *
 * @author xBlackCat
 */

public enum PreviewIcon implements Icon {
    CopyToClipBoard("preview-copylink.png"),
    Load("preview-load.png"),
    Loading("preview-loading.png");

    private final IResourceIcon icon;

    private PreviewIcon(String filename) {
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
