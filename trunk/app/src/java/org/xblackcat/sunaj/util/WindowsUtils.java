package org.xblackcat.sunaj.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Date: 16/10/2006
 *
 * @author xBlackCat
 */

public class WindowsUtils {
    private static final String BUTTON_IMAGES_PREFIX = "/images/button/";
    public static final Insets EMPTY_MARGIN = new Insets(0, 2, 0, 2);

    private WindowsUtils() {
    }

    public static void moveToScreenCenter(Window window) {
        int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() - window.getWidth()) >> 1;
        int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - window.getHeight()) >> 1;
        window.setLocation(x, y);
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
        JPanel cover = new JPanel(new FlowLayout(align, 0, 0));
        cover.add(comp);
        cover.setBackground(comp.getBackground());
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
        toggleButton.setIcon(ImageUtils.getIcon(imageSet + "enabled.png"));
        toggleButton.setHorizontalAlignment(SwingConstants.CENTER);
        toggleButton.setBorder(null);
        toggleButton.setBackground(Color.white);
        toggleButton.setFocusPainted(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setMargin(EMPTY_MARGIN);
        toggleButton.setRolloverEnabled(true);
        toggleButton.setSelectedIcon(ImageUtils.getIcon(imageSet + "pressed.png"));
        toggleButton.setRolloverIcon(ImageUtils.getIcon(imageSet + "over.png"));
        toggleButton.setRolloverSelectedIcon(ImageUtils.getIcon(imageSet + "pressed.png"));
        toggleButton.setDisabledIcon(ImageUtils.getIcon(imageSet + "disabled.png"));
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
        button.setIcon(ImageUtils.getIcon(imageSet + "enabled.png"));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBorder(null);
        button.setBackground(Color.white);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMargin(EMPTY_MARGIN);
        button.setRolloverEnabled(true);
        button.setSelectedIcon(ImageUtils.getIcon(imageSet + "pressed.png"));
        button.setRolloverIcon(ImageUtils.getIcon(imageSet + "over.png"));
        button.setRolloverSelectedIcon(ImageUtils.getIcon(imageSet + "pressed.png"));
        button.setDisabledIcon(ImageUtils.getIcon(imageSet + "disabled.png"));
        button.addActionListener(action);
        button.setOpaque(false);
        return button;
    }
}
