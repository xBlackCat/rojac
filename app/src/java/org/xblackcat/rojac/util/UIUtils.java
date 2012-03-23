package org.xblackcat.rojac.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.rojac.gui.theme.IResourceIcon;
import org.xblackcat.rojac.gui.theme.IconPack;
import org.xblackcat.rojac.service.options.Property;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

/**
 * @author xBlackCat
 */

public final class UIUtils {
    public static final Log log = LogFactory.getLog(UIUtils.class);

    private UIUtils() {
    }

    public static void setLookAndFeel(LookAndFeel laf) throws UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(laf);

        for (Window f : Frame.getWindows()) {
            SwingUtilities.updateComponentTreeUI(f);

            deepInvalidate(f);

            Dimension size = f.getSize();
            f.pack();
            f.setSize(size);
        }
    }

    public static void deepInvalidate(Container comp) {
        for (Component c : comp.getComponents()) {
            c.invalidate();
            c.validate();
            c.repaint();
            if (c instanceof Container) {
                deepInvalidate((Container) c);
            }
        }
    }

    public static LookAndFeel getDefaultLAFClass() {
        String lafClassName = UIManager.getSystemLookAndFeelClassName();

        LookAndFeel laf = null;
        try {
            laf = (LookAndFeel) ResourceUtils.loadObjectOrEnum(lafClassName);
        } catch (ClassNotFoundException e) {
            log.debug("System L&F class " + lafClassName + " not found.", e);
        } catch (IllegalAccessException e) {
            log.debug("System L&F class " + lafClassName + " can not be accessed.", e);
        } catch (InstantiationException e) {
            log.debug("System L&F class " + lafClassName + " can not be initialized.", e);
        }

        if (laf == null) {
            laf = new MetalLookAndFeel();
        }

        return laf;
    }

    /**
     * Creates a new <code>Color</code> that is a brighter version of this <code>Color</code>.
     * <p/>
     * This method applies an arbitrary scale factor to each of the three RGB components of this <code>Color</code> to
     * create a brighter version of this <code>Color</code>. Although <code>brighter</code> and <code>darker</code> are
     * inverse operations, the results of a series of invocations of these two methods might be inconsistent because of
     * rounding errors.
     *
     * @param c      initial color
     * @param factor brightness factor
     * @return a new <code>Color</code> object that is a brighter version of this <code>Color</code>.
     * @see java.awt.Color#darker
     * @since JDK1.0
     */
    public static Color brighter(Color c, double factor) {
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();

        /* From 2D group:
         * 1. black.brighter() should return grey
         * 2. applying brighter to blue will always return blue, brighter
         * 3. non pure color (non zero rgb) will eventually return white
         */
        int i = (int) (1.0 / (1.0 - factor));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i);
        }
        if (r > 0 && r < i) {
            r = i;
        }
        if (g > 0 && g < i) {
            g = i;
        }
        if (b > 0 && b < i) {
            b = i;
        }

        return new Color(Math.min((int) (r / factor), 255),
                Math.min((int) (g / factor), 255),
                Math.min((int) (b / factor), 255));
    }

    public static Icon getIcon(IResourceIcon icon) {
        if (icon == null) {
            return null;
        }

        IconPack imagePack = Property.ROJAC_GUI_ICONPACK.get();
        return imagePack.getIcon(icon);
    }

    public static void updateBackground(Component c, Color color) {
        c.setBackground(color);

        if (c instanceof Container) {
            Container cont = (Container) c;
            int i = 0;
            int size = cont.getComponentCount();

            while (i < size) {
                updateBackground(cont.getComponent(i++), color);
            }
        }
    }
}
