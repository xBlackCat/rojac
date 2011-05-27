package org.xblackcat.rojac.gui.theme;

import java.awt.*;

/**
 * @author xBlackCat
 */

public class TextStyle {
    public static final TextStyle DEFAULT = new TextStyle(null, null, null);

    private final Font font;
    private final Color foreground;
    private final Color background;

    public TextStyle(Font font, Color foreground, Color background) {
        this.font = font;
        this.foreground = foreground;
        this.background = background;
    }

    /**
     * Returns customized font. Return <code>null</code> means </b>use default</b>
     *
     * @return
     */
    public Font getFont() {
        return font;
    }

    /**
     * Returns customized background color. Return <code>null</code> means </b>use default</b>
     *
     * @return
     */
    public Color getBackground() {
        return background;
    }

    /**
     * Returns customized foreground color. Return <code>null</code> means </b>use default</b>
     *
     * @return
     */
    public Color getForeground() {
        return foreground;
    }
}
