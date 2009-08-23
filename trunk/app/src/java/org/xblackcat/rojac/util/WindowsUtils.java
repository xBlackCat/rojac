package org.xblackcat.rojac.util;

import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.RojacHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Date: 16/10/2006
 *
 * @author xBlackCat
 */

public final class WindowsUtils {
    private static final String BUTTON_IMAGES_PREFIX = "button/";
    public static final Insets EMPTY_MARGIN = new Insets(0, 2, 0, 2);

    private WindowsUtils() {
    }

    public static void showTestFrame(Container contentPane) {
        JFrame f = new JFrame("Message pane test");

        f.setContentPane(contentPane);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        f.setSize(300, 300);

        WindowsUtils.centerOnScreen(f);
    }

    /**
     * Cover component by JPanel with FlowLayout (align - CENTER)
     *
     * @param comp component to cover.
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
     * @param comp       component to cover.
     * @param align      aling type. See {@linkplain java.awt.FlowLayout#FlowLayout(int)} for available values.
     * @param background
     *
     * @return covered component.
     */
    public static Component coverComponent(Component comp, int align, Color background) {
        JPanel cover = new JPanel(new FlowLayout(align, 0, 0));
        cover.add(comp);
        cover.setBackground(background);
        return cover;
    }

    public static void setComponentFixedSize(JComponent component, Dimension size) {
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setSize(size);
    }

    public static JToggleButton setupToggleButton(String imageSet, ActionListener action, Messages mes) {
        return setupToggleButton(imageSet, action, mes, null);
    }

    public static JToggleButton setupToggleButton(String buttonName, ActionListener action, Messages mes, ButtonGroup bg) {
        String imageSet = BUTTON_IMAGES_PREFIX + buttonName + '/';
        JToggleButton toggleButton = new JToggleButton();
        toggleButton.setIcon(RojacHelper.loadIcon(imageSet + "enabled.png"));
        toggleButton.setHorizontalAlignment(SwingConstants.CENTER);
        toggleButton.setBorder(null);
        toggleButton.setBackground(Color.white);
        toggleButton.setFocusPainted(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setMargin(EMPTY_MARGIN);
        toggleButton.setRolloverEnabled(true);
        toggleButton.setSelectedIcon(RojacHelper.loadIcon(imageSet + "pressed.png"));
        toggleButton.setRolloverIcon(RojacHelper.loadIcon(imageSet + "over.png"));
        toggleButton.setRolloverSelectedIcon(RojacHelper.loadIcon(imageSet + "pressed.png"));
        toggleButton.setDisabledIcon(RojacHelper.loadIcon(imageSet + "disabled.png"));
        toggleButton.addActionListener(action);
        toggleButton.setOpaque(false);
        toggleButton.setToolTipText(mes.get());
        if (bg != null) {
            bg.add(toggleButton);
        }
        return toggleButton;
    }

    public static JButton setupButton(Messages text, ActionListener action, Messages tooltip) {
        JButton button = new JButton();
        button.setIcon(null);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBackground(Color.white);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.setSelectedIcon(null);
        button.setRolloverIcon(null);
        button.setRolloverSelectedIcon(null);
        button.setDisabledIcon(null);
        button.addActionListener(action);
        button.setToolTipText(tooltip.get());
        button.setText(text.get());
        return button;
    }

    public static JButton setupImageButton(String buttonName, ActionListener action, Messages tooltip) {
        return setupImageButton(buttonName, action, tooltip.get());
    }

    public static JButton setupImageButton(String buttonName, ActionListener action, String tooltip) {
        String imageSet = BUTTON_IMAGES_PREFIX + buttonName + '/';
        JButton button = new JButton();
        button.setIcon(RojacHelper.loadIcon(imageSet + "enabled.png"));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBorder(null);
        button.setBackground(Color.white);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMargin(EMPTY_MARGIN);
        button.setRolloverEnabled(true);
        button.setSelectedIcon(RojacHelper.loadIcon(imageSet + "pressed.png"));
        button.setRolloverIcon(RojacHelper.loadIcon(imageSet + "over.png"));
        button.setRolloverSelectedIcon(RojacHelper.loadIcon(imageSet + "pressed.png"));
        button.setDisabledIcon(RojacHelper.loadIcon(imageSet + "disabled.png"));
        button.addActionListener(action);
        button.setOpaque(false);
        button.setToolTipText(tooltip);
        return button;
    }

    public static final void centerOnScreen(Window window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowSize = window.getSize();

        if (windowSize.height > screenSize.height) {
            windowSize.height = screenSize.height;
        }

        if (windowSize.width > screenSize.width) {
            windowSize.width = screenSize.width;
        }

        window.setLocation((screenSize.width - windowSize.width) / 2,
                (screenSize.height - windowSize.height) / 2);
    }

    public static void center(Window window, Component parent) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Rectangle bounds = new Rectangle(parent.getLocationOnScreen(), parent.getSize());

        int w = window.getWidth();
        int h = window.getHeight();

        // center according to parent

        int x = ((int) bounds.getCenterX()) - w / 2;
        int y = ((int) bounds.getCenterY()) - h / 2;

        // does it fit on screen?

        if (x < 0) {
            x = 0;
        } else if (x + w > screenSize.getWidth()) {
            x = ((int) screenSize.getWidth()) - w;
        }

        if (y < 0) {
            y = 0;
        } else if (y + h > screenSize.getHeight()) {
            y = ((int) screenSize.getHeight()) - h;
        }

        // done

        window.setBounds(x, y, w, h);
    }

    public static JToolBar createToolBar(JComponent... components) {
        JToolBar toolBar = new JToolBar();

        for (JComponent c : components) {
            toolBar.add(c);
        }

        return toolBar;
    }
}
