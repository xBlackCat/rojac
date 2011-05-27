package org.xblackcat.rojac.gui.component;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * @author xBlackCat
 */
public class JComponentDelegate extends JComponent {
    private final Collection<? extends JComponent> components;

    public JComponentDelegate(Collection<? extends JComponent> components) {
        if (components == null) {
            throw new NullPointerException("Delegate can not be initialized with null.");
        }
        this.components = components;
    }

    @Override
    public void setForeground(Color fg) {
        for (JComponent c : components) {
            if (c != null) {
                c.setForeground(fg);
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        for (JComponent c : components) {
            if (c != null) {
                c.setEnabled(enabled);
            }
        }
    }

    @Override
    public void setBackground(Color bg) {
        for (JComponent c : components) {
            if (c != null) {
                c.setBackground(bg);
            }
        }
    }

    @Override
    public void setFont(Font font) {
        for (JComponent c : components) {
            if (c != null) {
                c.setFont(font);
            }
        }
    }
}
