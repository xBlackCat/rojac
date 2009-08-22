package org.xblackcat.rojac.util;

import org.flexdock.util.SwingUtility;
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

        SwingUtility.centerOnScreen(f);
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
}
