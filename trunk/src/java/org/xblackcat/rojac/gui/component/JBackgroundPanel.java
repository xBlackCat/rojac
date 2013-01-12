package org.xblackcat.rojac.gui.component;

import javax.swing.*;
import java.awt.*;

/**
 * @author ASUS
 */

public class JBackgroundPanel extends JPanel {
    private final Image image;
    private Fill fillType;

    public JBackgroundPanel(Image image, Fill fillType) {
        this.image = image;
        this.fillType = fillType;
    }

    public JBackgroundPanel(Image image, Fill fillType, boolean isDoubleBuffered) {
        super(isDoubleBuffered);
        this.image = image;
        this.fillType = fillType;
    }

    public JBackgroundPanel(Image image, Fill fillType, LayoutManager layout) {
        super(layout);
        this.image = image;
        this.fillType = fillType;
    }

    public JBackgroundPanel(Image image, Fill fillType, LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
        this.image = image;
        this.fillType = fillType;
    }

    public JBackgroundPanel(Image image) {
        this(image, Fill.Center);
    }

    public JBackgroundPanel(Image image, boolean isDoubleBuffered) {
        this(image, Fill.Center, isDoubleBuffered);
    }

    public JBackgroundPanel(Image image, LayoutManager layout) {
        this(image, Fill.Center, layout);
    }

    public JBackgroundPanel(Image image, LayoutManager layout, boolean isDoubleBuffered) {
        this(image, Fill.Center, layout, isDoubleBuffered);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            int ih = image.getHeight(this);
            int iw = image.getWidth(this);

            int w = getWidth();
            int h = getHeight();

            int posX = 0;
            int posY = 0;

            switch (fillType) {
                case Center:
                    posX = (w - iw) >> 1;
                    posY = (h - ih) >> 1;
                    break;
                case LeftTop:
                    posX = 0;
                    posY = 0;
                    break;
                case Left:
                    posX = 0;
                    posY = (h - ih) >> 1;
                    break;
                case LeftBottom:
                    posX = 0;
                    posY = h - ih;
                    break;
                case Bottom:
                    posX = (w - iw) >> 1;
                    posY = h - ih;
                    break;
                case RightBottom:
                    posX = w - iw;
                    posY = h - ih;
                    break;
                case Right:
                    posX = w - iw;
                    posY = (h - ih) >> 1;
                    break;
                case RightTop:
                    posX = w - iw;
                    posY = 0;
                    break;
                case Top:
                    posX = (w - iw) >> 1;
                    posY = 0;
                    break;
                case Tile:
                    int y = 0;
                    while (y < h) {
                        int x = 0;
                        while (x < w) {
                            g.drawImage(image, x, y, iw, ih, this);
                            x += iw;
                        }

                        y += ih;
                    }
                    return;
                case Stretch:
                    g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
                    return;
                case StretchProportional:
                    double aspect = h / (double) w;
                    double aspectI = ih / (double) iw;
                    if (aspect > aspectI) {
                        iw = w;
                        ih = (int) (w * aspectI);
                    } else {
                        iw = (int) (h / aspectI);
                        ih = h;
                    }

                    posX = (w - iw) >> 1;
                    posY = (h - ih) >> 1;
                    break;
            }

            // Draw
            g.drawImage(image, posX, posY, iw, ih, this);
        }
    }
}

