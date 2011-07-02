package org.xblackcat.rojac.gui;

import java.awt.*;

/**
 * @author xBlackCat
 */
public class MainFrameState implements IState {
    private final int windowState;
    private final Point location;
    private final Dimension size;

    public MainFrameState(int windowState, Point location, Dimension size) {
        this.windowState = windowState;
        this.location = location;
        this.size = size;
    }

    public MainFrameState changeState(int windowState) {
        return new MainFrameState(windowState, location, size);
    }

    public int getWindowState() {
        return windowState;
    }

    public Point getLocation() {
        return location;
    }

    public Dimension getSize() {
        return size;
    }
}
