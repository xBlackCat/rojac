package org.xblackcat.sunaj.util;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xblackcat.utils.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Date: 16/10/2006
 *
 * @author xBlackCat
 */

public class WindowsUtils {
    private static final Log log = LogFactory.getLog(WindowsUtils.class);

    private static final String BUTTON_IMAGES_PREFIX = "/images/button/";
    public static final Insets EMPTY_MARGIN = new Insets(0, 2, 0, 2);

    private WindowsUtils() {
    }

    public static void showTestFrame(Container contentPane, boolean javaLAF) {
        if (!javaLAF) {
            try {
                UIManager.setLookAndFeel(new WindowsLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                log.error("Can not initialize Windows L&F. The default L&F will be used", e);
            }
        }
        JFrame f = new JFrame("Message pane test");

        f.setContentPane(contentPane);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setSize(300, 300);

        WindowsUtils.moveToScreenCenter(f);
    }

    public static void moveToScreenCenter(Window window) {
        int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - window.getWidth()) >> 1;
        int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - window.getHeight()) >> 1;
        window.setLocation(x, y);
    }

    public static void moveToComponentCenter(Window window, Component c) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Point p = c.getLocationOnScreen();

        Dimension cs = c.getSize();
        int x = (cs.width - window.getWidth() >> 1) + p.x;
        int y = (cs.height - window.getHeight() >> 1) + p.y;

        if (x + window.getWidth() > screenSize.width) {
            x = screenSize.width - window.getWidth();
        }

        if (y + window.getHeight() > screenSize.height) {
            y = screenSize.height - window.getHeight();
        }

        if (x < 0) {
            x = 0;
        }

        if (y < 0) {
            y = 0;
        }

        window.setLocation(x, y);
    }

    /**
     * Cover component by JPanel with FlowLayout (align - CENTER)
     *
     * @param comp  component to cover.
     *
     * @return covered component.
     */
    public static Component coverComponent(Component comp) {
        return coverComponent(comp, FlowLayout.CENTER, comp.getBackground());
    }
    /**
     * Cover component by JPanel.
     *
     * @param comp  component to cover.
     * @param align aling type. See {@linkplain java.awt.FlowLayout#FlowLayout(int)} for available values.
     *
     * @return covered component.
     */
    public static Component coverComponent(Component comp, int align) {
        return coverComponent(comp, align, comp.getBackground());
    }

    /**
     * Cover component by JPanel.
     *
     * @param comp  component to cover.
     * @param align aling type. See {@linkplain java.awt.FlowLayout#FlowLayout(int)} for available values.
     *
     * @param background
     * @return covered component.
     */
    public static Component coverComponent(Component comp, int align, Color background) {
        JPanel cover = new JPanel(new FlowLayout(align, 0, 0));
        cover.add(comp);
        cover.setBackground(background);
        return cover;
    }

    public static JMenuItem setupMenuItem(String name, String command, Icon icon, ActionListener listener, boolean enabled) {
        JMenuItem mi;
        mi = new JMenuItem(name, icon);
        mi.setActionCommand(command);
        mi.addActionListener(listener);
        mi.setEnabled(enabled);
        return mi;
    }

    public static JMenuItem setupCheckMenuItem(String name, String command, ActionListener listener, boolean selected, ButtonGroup bg) {
        JMenuItem mi;
        mi = new JRadioButtonMenuItem(name);
        mi.setActionCommand(command);
        mi.addActionListener(listener);
        mi.setSelected(selected);
        bg.add(mi);
        return mi;
    }

    public static void setComponentFixedSize(JComponent component, Dimension size) {
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setSize(size);
    }

    public static JToggleButton setupToggleButton(String imageSet, ActionListener action, ButtonGroup bg) {
        JToggleButton toggleButton = new JToggleButton();
        toggleButton.setIcon(ResourceUtils.loadImageIcon(imageSet + "enabled.png"));
        toggleButton.setHorizontalAlignment(SwingConstants.CENTER);
        toggleButton.setBorder(null);
        toggleButton.setBackground(Color.white);
        toggleButton.setFocusPainted(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setMargin(EMPTY_MARGIN);
        toggleButton.setRolloverEnabled(true);
        toggleButton.setSelectedIcon(ResourceUtils.loadImageIcon(imageSet + "pressed.png"));
        toggleButton.setRolloverIcon(ResourceUtils.loadImageIcon(imageSet + "over.png"));
        toggleButton.setRolloverSelectedIcon(ResourceUtils.loadImageIcon(imageSet + "pressed.png"));
        toggleButton.setDisabledIcon(ResourceUtils.loadImageIcon(imageSet + "disabled.png"));
        toggleButton.addActionListener(action);
        toggleButton.setOpaque(false);
        if (bg != null) {
            bg.add(toggleButton);
        }
        return toggleButton;
    }

    public static JButton setupButton(String buttonName, ActionListener action) {
        String imageSet = BUTTON_IMAGES_PREFIX + buttonName + '/';
        JButton button = new JButton();
        button.setIcon(ResourceUtils.loadImageIcon(imageSet + "enabled.png"));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBorder(null);
        button.setBackground(Color.white);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMargin(EMPTY_MARGIN);
        button.setRolloverEnabled(true);
        button.setSelectedIcon(ResourceUtils.loadImageIcon(imageSet + "pressed.png"));
        button.setRolloverIcon(ResourceUtils.loadImageIcon(imageSet + "over.png"));
        button.setRolloverSelectedIcon(ResourceUtils.loadImageIcon(imageSet + "pressed.png"));
        button.setDisabledIcon(ResourceUtils.loadImageIcon(imageSet + "disabled.png"));
        button.addActionListener(action);
        button.setOpaque(false);
        return button;
    }
}
