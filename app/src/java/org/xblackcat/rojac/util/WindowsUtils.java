package org.xblackcat.rojac.util;

import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.RojacHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.MissingResourceException;

/**
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


    public static JToggleButton setupToggleButton(String imageSet, ActionListener action, Messages mes) {
        return setupToggleButton(imageSet, action, mes, null);
    }

    public static JToggleButton setupToggleButton(String buttonName, ActionListener action, Messages mes, ButtonGroup bg) {
        String imageSet = BUTTON_IMAGES_PREFIX + buttonName + '/';
        // Load icons for the button.
        // If default icon is not present a MissingResourceException will be thrown.
        String extension = "png";
        Icon defaultIcon = RojacHelper.loadIcon(imageSet + "enabled." + extension);
        Icon selectedIcon;
        try {
            selectedIcon = RojacHelper.loadIcon(imageSet + "pressed." + extension);
        } catch (MissingResourceException e) {
            // Use default icon
            selectedIcon = defaultIcon;
        }
        Icon hoverIcon;
        try {
            hoverIcon = RojacHelper.loadIcon(imageSet + "over." + extension);
        } catch (MissingResourceException e) {
            // Use default icon
            hoverIcon = defaultIcon;
        }
        Icon disabledIcon;
        try {
            disabledIcon = RojacHelper.loadIcon(imageSet + "disabled." + extension);
        } catch (MissingResourceException e) {
            // Use default icon
            disabledIcon = defaultIcon;
        }

        JToggleButton toggleButton = new JToggleButton();
        toggleButton.setIcon(defaultIcon);
        toggleButton.setHorizontalAlignment(SwingConstants.CENTER);
        toggleButton.setBorder(null);
        toggleButton.setBackground(Color.white);
        toggleButton.setFocusPainted(false);
        toggleButton.setBorderPainted(false);
        toggleButton.setMargin(EMPTY_MARGIN);
        toggleButton.setRolloverEnabled(true);
        toggleButton.setSelectedIcon(selectedIcon);
        toggleButton.setRolloverIcon(hoverIcon););
        toggleButton.setRolloverSelectedIcon(selectedIcon);
        toggleButton.setDisabledIcon(disabledIcon);
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

    /**
     * Initializes a button with image. Button name define images set. Each button has four images for four button
     * states: normal, disabled, hovered and pressed.
     *
     * @param buttonName identifier of images set for the button.
     * @param action     action listener to fire when action is performed.
     * @param toolTip    localized message identifier.
     *
     * @return initialized button.
     */
    public static JButton setupImageButton(String buttonName, ActionListener action, Messages toolTip) {
        String imageSet = BUTTON_IMAGES_PREFIX + buttonName + '/';
        // Load icons for the button.
        // If default icon is not present a MissingResourceException will be thrown.
        String extension = "png";
        Icon defaultIcon = RojacHelper.loadIcon(imageSet + "enabled." + extension);
        Icon selectedIcon;
        try {
            selectedIcon = RojacHelper.loadIcon(imageSet + "pressed." + extension);
        } catch (MissingResourceException e) {
            // Use default icon
            selectedIcon = defaultIcon;
        }
        Icon hoverIcon;
        try {
            hoverIcon = RojacHelper.loadIcon(imageSet + "over." + extension);
        } catch (MissingResourceException e) {
            // Use default icon
            hoverIcon = defaultIcon;
        }
        Icon disabledIcon;
        try {
            disabledIcon = RojacHelper.loadIcon(imageSet + "disabled." + extension);
        } catch (MissingResourceException e) {
            // Use default icon
            disabledIcon = defaultIcon;
        }

        JButton button = new JButton();
        button.setIcon(defaultIcon);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBorder(null);
        button.setBackground(Color.white);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setMargin(EMPTY_MARGIN);
        button.setRolloverEnabled(true);
        button.setSelectedIcon(selectedIcon);
        button.setRolloverIcon(hoverIcon);
        button.setRolloverSelectedIcon(selectedIcon);
        button.setDisabledIcon(disabledIcon);
        button.addActionListener(action);
        button.setOpaque(false);
        button.setToolTipText(toolTip.get());
        return button;
    }

    public static void centerOnScreen(Window window) {
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

    public static Component createButtonsBar(AButtonAction... buttons) {
        return createButtonsBar(null, null, buttons);
    }

    public static Component createButtonsBar(JDialog dlg, Messages defAction, AButtonAction... buttons) {
        JPanel buttonPane = new JPanel(new GridLayout(1, 0, 10, 5));

        for (AButtonAction ba : buttons) {
            JButton b = WindowsUtils.setupButton(ba.getMessage(), ba, ba.getMessage());
            if (ba.getMessage() == defAction) {
                dlg.getRootPane().setDefaultButton(b);
            }
            buttonPane.add(b);
        }

        return WindowsUtils.coverComponent(buttonPane, FlowLayout.CENTER);
    }
}
