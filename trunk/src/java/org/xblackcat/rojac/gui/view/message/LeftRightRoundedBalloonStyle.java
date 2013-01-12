package org.xblackcat.rojac.gui.view.message;

import net.java.balloontip.styles.BalloonTipStyle;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * A balloon tip with rounded corners and a one pixel border
 * Tip shown on left or right side of component
 */
public class LeftRightRoundedBalloonStyle extends BalloonTipStyle {

    private final int arcWidth;
    private final int arcHeight;

    private final Color fillColor;
    private final Color borderColor;

    /**
     * Constructor
     *
     * @param arcWidth    width of the rounded corner
     * @param arcHeight   height of the rounded color
     * @param fillColor   fill color
     * @param borderColor border line color
     */
    public LeftRightRoundedBalloonStyle(int arcWidth, int arcHeight, Color fillColor, Color borderColor) {
        super();
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.fillColor = fillColor;
        this.borderColor = borderColor;
    }

    public Insets getBorderInsets(Component c) {
        if (flipX) {
            return new Insets(arcHeight, horizontalOffset + arcWidth, arcHeight, arcWidth);
        }
        return new Insets(arcHeight, arcWidth, arcHeight, arcWidth + horizontalOffset);
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g;
        width -= 1;
        height -= 1;

        int xLeft;        // Y-coordinate of the top side of the balloon
        int xRight;    // Y-coordinate of the bottom side of the balloon
        if (flipX) {
            xLeft = x + horizontalOffset;
            xRight = x + width;
        } else {
            xLeft = x;
            xRight = x + width - horizontalOffset;
        }

        // Draw the outline of the balloon
        GeneralPath outline = new GeneralPath();
        outline.moveTo(xLeft, y + arcHeight);

        outline.quadTo(xLeft, y, xLeft + arcWidth, y);
        outline.lineTo(xRight - arcWidth, y);
        outline.quadTo(xRight, y, xRight, y + arcHeight);

        if (!flipX) {
            if (!flipY) {
                outline.lineTo(xRight, y + verticalOffset);
                outline.lineTo(xRight + horizontalOffset, y + verticalOffset);
                outline.lineTo(xRight, y + horizontalOffset + verticalOffset);
            } else {
                outline.lineTo(xRight, y + height - horizontalOffset - verticalOffset);
                outline.lineTo(xRight + horizontalOffset, y + height - verticalOffset);
                outline.lineTo(xRight, y + height - verticalOffset);
            }
        }

        outline.lineTo(xRight, y + height - arcHeight);
        outline.quadTo(xRight, y + height, xRight - arcWidth, y + height);
        outline.lineTo(xLeft + arcWidth, y + height);
        outline.quadTo(xLeft, y + height, xLeft, y + height - arcHeight);

        if (flipX) {
            if (!flipY) {
                outline.lineTo(xLeft, y + horizontalOffset + verticalOffset);
                outline.lineTo(xLeft - horizontalOffset, y + verticalOffset);
                outline.lineTo(xLeft, y + verticalOffset);
            } else {
                outline.lineTo(xLeft, y + height - verticalOffset);
                outline.lineTo(xLeft - horizontalOffset, y + height - verticalOffset);
                outline.lineTo(xLeft, y + height - horizontalOffset - verticalOffset);
            }
        }

        outline.closePath();

        g2d.setPaint(fillColor);
        g2d.fill(outline);
        g2d.setPaint(borderColor);
        g2d.draw(outline);
    }

    public int getMinimalHorizontalOffset() {
        return arcWidth + verticalOffset;
    }


}
