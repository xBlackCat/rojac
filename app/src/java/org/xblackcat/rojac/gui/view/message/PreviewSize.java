package org.xblackcat.rojac.gui.view.message;

import java.awt.*;

/**
 * 28.03.12 17:48
 *
 * @author xBlackCat
 */
public enum PreviewSize {
    S_320(320, 240),
    S_400(400, 300),
    S_640(640, 480),
    S_800(800, 600),
    S_1024(1024, 764),
    S_1280(1280, 960),
    S_1600(1600, 1200),
    // 16:9
    W_320(320, 180),
    W_400(400, 225),
    W_640(640, 360),
    W_800(800, 450),
    W_1024(1024, 576),
    W_1280(1280, 720),
    W_1600(1600, 900);

    private final int width;
    private final int height;

    private PreviewSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public String asString() {
        return String.format("%dx%d", width, height);
    }
}
