package org.xblackcat.rojac.gui.component;

import javax.swing.*;
import java.awt.*;

/**
 * @author ASUS
 */

public class JBackgroundPanel extends JPanel {
    private final Image image;
    private final int verticalShift;

    public JBackgroundPanel(Image image, int verticalShift) {
        this.image = image;
        this.verticalShift = verticalShift;
    }

    public JBackgroundPanel(Image image, int verticalShift, boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        this.image = image;
        this.verticalShift = verticalShift;
    }

    public JBackgroundPanel(Image image, int verticalShift, LayoutManager layout) {
        super(layout);
        this.image = image;
        this.verticalShift = verticalShift;
    }

    public JBackgroundPanel(Image image, int verticalShift, LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        this.image = image;
        this.verticalShift = verticalShift;
    }

    public JBackgroundPanel(Image image) {
        this(image, 0);
    }

    public JBackgroundPanel(Image image, boolean isDoubleBuffered) {
        this(image, 0, isDoubleBuffered);
    }

    public JBackgroundPanel(Image image, LayoutManager layout) {
        this(image, 0, layout);
    }

    public JBackgroundPanel(Image image, LayoutManager layout, boolean isDoubleBuffered) {
        this(image, 0, layout, isDoubleBuffered);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int ih = image.getHeight(this);
            int iw = image.getWidth(this);

            int w = getWidth();

            int x = 0;
            while (x < w) {
                g.drawImage(image, x, -verticalShift, iw, ih, this);
                x += iw;
            }
        }
    }
}

