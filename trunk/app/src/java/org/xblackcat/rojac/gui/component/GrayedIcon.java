package org.xblackcat.rojac.gui.component;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author xBlackCat
 */
public class GrayedIcon implements Icon {
    private final Icon parent;

    public GrayedIcon(Icon base) {
        BufferedImage image = new BufferedImage(base.getIconWidth(), base.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        base.paintIcon(null, image.getGraphics(), 0, 0);

        this.parent = new ImageIcon(GrayFilter.createDisabledImage(image));
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        parent.paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
        return parent.getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return parent.getIconHeight();
    }
}
