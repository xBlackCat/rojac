package org.xblackcat.rojac.util;

import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.theme.IButtonIcons;
import org.xblackcat.rojac.i18n.Messages;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;
import java.awt.*;

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


    public static JToggleButton setupToggleButton(String imageSet, AButtonAction action) {
        return setupToggleButton(imageSet, action,  null);
    }

    public static JToggleButton setupToggleButton(String buttonName, AButtonAction action, ButtonGroup bg) {
        IButtonIcons icons = Property.ROJAC_GUI_ICONPACK.get().getImageButtonIcons(buttonName);

        JToggleButton toggleButton = new JToggleButton();
        setupImageButton(toggleButton, icons, action);
        if (bg != null) {
            bg.add(toggleButton);
        }
        return toggleButton;
    }

    public static JButton setupButton(AButtonAction action) {
        return setupButton(action, action.getMessage());
    }

    public static JButton setupButton(AButtonAction action, Messages toolTip) {
        IButtonIcons icons = Property.ROJAC_GUI_ICONPACK.get().getButtonIcons();

        JButton button = new JButton();

        // Set icons
        button.setIcon(icons.getDefaultIcon());
        button.setSelectedIcon(icons.getSelectedIcon());
        button.setRolloverIcon(icons.getRolloverIcon());
        button.setRolloverSelectedIcon(icons.getRolloverSelectedIcon());
        button.setDisabledIcon(icons.getDisabledIcon());

        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setBackground(Color.white);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
        button.addActionListener(action);

        // Set texts
        button.setToolTipText(toolTip.get());
        button.setText(action.getMessage().get());
        return button;
    }

    /**
     * Initializes a button with image. Button name define images set. Each button has four images for four button
     * states: normal, disabled, hovered and pressed.
     *
     * @param buttonName identifier of images set for the button.
     * @param action     action listener to fire when action is performed.
     *
     * @return initialized button.
     */
    public static JButton setupImageButton(String buttonName, AButtonAction action) {
        IButtonIcons icons = Property.ROJAC_GUI_ICONPACK.get().getImageButtonIcons(buttonName);

        JButton button = new JButton();
        setupImageButton(button, icons, action);

        return button;
    }

    private static void setupImageButton(AbstractButton button, IButtonIcons icons, AButtonAction action) {
        // Set common parameters
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.BOTTOM);
//        button.setBorder(null);
        button.setBackground(Color.white);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
//        button.setMargin(EMPTY_MARGIN);
        button.setRolloverEnabled(true);
        button.setOpaque(false);

        // Set icons
        button.setIcon(icons.getDefaultIcon());
        button.setSelectedIcon(icons.getSelectedIcon());
        button.setRolloverIcon(icons.getRolloverIcon());
        button.setRolloverSelectedIcon(icons.getRolloverSelectedIcon());
        button.setDisabledIcon(icons.getDisabledIcon());

        // Set other options
        button.addActionListener(action);
        button.setToolTipText(action.getMessage().get());
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
        if (parent == null || !parent.isVisible()) {
            // Parent is not visible - center window on a screen
            centerOnScreen(window);
            return;
        }

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
            if (c != null) {
                toolBar.add(c);
            } else {
                toolBar.addSeparator();
            }
        }

        return toolBar;
    }

    /**
     * Constructs a container for buttons and fill the container with the buttons. Default alignment (CENTER) for
     * buttons is used.
     *
     * @param buttons button properties to be placed to buttons container.
     *
     * @return container with constructed buttons.
     */
    public static Component createButtonsBar(AButtonAction... buttons) {
        return createButtonsBar(null, null, buttons);
    }

    /**
     * Constructs a container for buttons and fill the container with the buttons. Specified alignment for buttons is
     * used.
     *
     * @param align   alignment of buttons in the container.
     * @param buttons button properties to be placed to buttons container.
     *
     * @return container with constructed buttons.
     */
    public static Component createButtonsBar(int align, AButtonAction... buttons) {
        return createButtonsBar(null, null, align, buttons);
    }

    /**
     * Constructs a container for buttons and fill the container with the buttons. Also sets up a dialog with default
     * button. Default button is determined by its message. Default alignment for buttons is used.
     *
     * @param dlg       dialog to be set with default button.
     * @param defAction message of default button.
     * @param buttons   button properties to be placed to buttons container.
     *
     * @return container with constructed buttons.
     */
    public static Component createButtonsBar(JDialog dlg, Messages defAction, AButtonAction... buttons) {
        return createButtonsBar(dlg, defAction, FlowLayout.CENTER, buttons);
    }

    /**
     * Constructs a container for buttons and fill the container with the buttons. Also sets up a dialog with default
     * button. Default button is determined by its message. Specified alignment for buttons is used.
     *
     * @param dlg       dialog to be set with default button.
     * @param defAction message of default button.
     * @param align   alignment of buttons in the container.
     * @param buttons   button properties to be placed to buttons container.
     *
     * @return container with constructed buttons.
     */
    public static Component createButtonsBar(JDialog dlg, Messages defAction, int align, AButtonAction... buttons) {
        if (dlg == null && defAction != null) {
            throw new IllegalArgumentException("Can not set default action if target dialog is not specified.");
        }
        JPanel buttonPane = new JPanel(new GridLayout(1, 0, 10, 5));

        for (AButtonAction ba : buttons) {
            JButton b = WindowsUtils.setupButton(ba);
            if (dlg != null && ba.getMessage() == defAction) {
                dlg.getRootPane().setDefaultButton(b);
            }
            buttonPane.add(b);
        }

        return WindowsUtils.coverComponent(buttonPane, align);
    }
}
