package org.xblackcat.rojac.gui.component;

import org.xblackcat.rojac.util.RojacUtils;
import org.xblackcat.utils.ResourceUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.MissingResourceException;

/**
 * 26.03.12 10:44
 *
 * @author xBlackCat
 */
public class RojacCursor {
    public static final RojacCursor ZoomIn = new RojacCursor("zoom-in", new Point(10, 5));
    public static final RojacCursor ZoomOut = new RojacCursor("zoom-out", new Point(10, 5));

    private static final RojacCursor[] CURSORS = new RojacCursor[]{
            ZoomIn,
            ZoomIn
    };

    public static void dropCache() {
        assert RojacUtils.checkThread(true);

        for (RojacCursor rc : CURSORS) {
            rc.cursor = null;
        }
    }

    private final String cursorName;
    private Cursor cursor;
    private final Point hotSpot;

    private RojacCursor(String cursorName, Point hotSpot) {
        this.cursorName = cursorName;
        this.hotSpot = hotSpot;
    }

    public Cursor get() {
        assert RojacUtils.checkThread(true);
        if (cursor == null) {
            // Load cursor
            try {
                Toolkit toolkit = Toolkit.getDefaultToolkit();

                Image image = ResourceUtils.loadImage("/images/cursor/" + cursorName + ".png");
                Dimension cursorSize = toolkit.getBestCursorSize(image.getWidth(null), image.getHeight(null));

                BufferedImage cursorImage = new BufferedImage(cursorSize.width, cursorSize.height, BufferedImage.TYPE_INT_ARGB);
                Graphics graphics = cursorImage.getGraphics();
                graphics.drawImage(image, 0, 0, null);

                cursor = toolkit.createCustomCursor(cursorImage, hotSpot, cursorName);
            } catch (MissingResourceException e) {
                cursor = Cursor.getDefaultCursor();
            }
        }

        return cursor;
    }
}
