package org.xblackcat.rojac.gui.view.message;

import net.java.balloontip.positioners.BasicBalloonTipPositioner;

import java.awt.*;

public class RightCenterPositioner extends BasicBalloonTipPositioner {
    public RightCenterPositioner(int hO, int vO) {
        super(hO, vO);
    }

    protected void determineLocation(Rectangle attached) {
        // First calculate the location, without applying any correction tricks
        int balloonWidth = balloonTip.getPreferredSize().width;
        int balloonHeight = balloonTip.getPreferredSize().height;
        flipX = false;
        flipY = false;

        hOffset = preferredHorizontalOffset;
        if (fixedAttachLocation) {
            x = new Float(attached.x + attached.width * attachLocationX).intValue() + balloonWidth;
            y = new Float(attached.y + attached.height * attachLocationY).intValue() + balloonHeight;
        } else {
            x = attached.x - balloonWidth;
            y = attached.y;
        }

        // Apply orientation correction
        if (orientationCorrection) {
            // Check collision with the top of the window
            if (y < 0) {
                flipY = true;
                if (fixedAttachLocation) {
                    y += balloonHeight;
                } else {
                    y = attached.y + attached.height - balloonHeight;
                }
            }

            // Check collision with the left side of the window
            if (x < 0) {
                flipX = true;
                if (fixedAttachLocation) {
                    x -= balloonWidth;
                } else {
                    x = attached.x + attached.width;
                }
                hOffset = balloonWidth - hOffset;
            }
        }

        // Apply offset correction
        if (offsetCorrection) {
            applyOffsetCorrection();
        }
    }
}
