package org.xblackcat.rojac.gui.component;

import javax.swing.*;
import java.awt.*;

/**
* @author xBlackCat
*/
public class LineRenderer extends JLabel {
    public LineRenderer(int alignment) {
        super();
        setHorizontalAlignment(alignment);
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
     * information.
     *
     * @since 1.5
     */
    public void invalidate() {
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
     * information.
     */
    public void validate() {
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
     * information.
     */
    public void revalidate() {
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
     * information.
     */
    public void repaint(long tm, int x, int y, int width, int height) {
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
     * information.
     */
    public void repaint(Rectangle r) {
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
     * information.
     *
     * @since 1.5
     */
    public void repaint() {
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
     * information.
     */
    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        // Strings get interned...
        if (propertyName == "text"
                || propertyName == "labelFor"
                || propertyName == "displayedMnemonic"
                || ((propertyName == "font" || propertyName == "foreground")
                && oldValue != newValue
                && getClientProperty(javax.swing.plaf.basic.BasicHTML.propertyKey) != null)) {

            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    /**
     * Overridden for performance reasons. See the <a href="#override">Implementation Note</a> for more
     * information.
     */
    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }
}
