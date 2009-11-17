package org.xblackcat.rojac.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

        setLookAndFeel(laf, Frame.getFrames());
    }

    private static void setLookAndFeel(LookAndFeel laf, Window... targets) {
        for (Window f : targets) {
            SwingUtilities.updateComponentTreeUI(f);
            setLookAndFeel(laf, f.getOwnedWindows());

            deepInvalidate(f);

            Dimension preferredSize = f.getPreferredSize();
            if (preferredSize.height > f.getHeight() || preferredSize.width > f.getWidth()) {
                f.setSize(preferredSize);
            }
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
}
