package org.xblackcat.rojac.util;

import org.xblackcat.rojac.gui.component.AButtonAction;
import org.xblackcat.rojac.gui.component.ShortCut;
import org.xblackcat.rojac.gui.theme.IButtonIcons;
import org.xblackcat.rojac.i18n.Message;
import org.xblackcat.rojac.service.options.Property;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author xBlackCat
 */

public final class WindowsUtils {
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
     * @return covered component.
     */
    public static Component coverComponent(Component comp, int align, Color background) {
        JPanel cover = new JPanel(new FlowLayout(align, 0, 0));
        cover.add(comp);
        cover.setBackground(background);
        return cover;
    }


    public static JToggleButton setupToggleButton(String imageSet, AButtonAction action) {
        return setupToggleButton(imageSet, action, null);
    }


    public static JToggleButton registerToggleButton(JComponent comp, String imageSet, AButtonAction action) {
        registerAction(comp, action);

        return setupToggleButton(imageSet, action, null);
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

    public static JButton setupButton(AButtonAction action, Message toolTip) {
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
        button.setToolTipText(getToolTip(toolTip, action.getShortCut()));
        button.setText(action.getMessage().get());
        return button;
    }

    /**
     * Initializes a button with image. Button name define images set. Each button has four images for four button
     * states: normal, disabled, hovered and pressed.
     *
     * @param buttonName identifier of images set for the button.
     * @param action     action listener to fire when action is performed.
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
        button.setBackground(Color.white);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setOpaque(false);

        // Set icons
        button.setIcon(icons.getDefaultIcon());
        button.setSelectedIcon(icons.getSelectedIcon());
        button.setRolloverIcon(icons.getRolloverIcon());
        button.setRolloverSelectedIcon(icons.getRolloverSelectedIcon());
        button.setDisabledIcon(icons.getDisabledIcon());

        // Set other options
        button.addActionListener(action);
        button.setToolTipText(getToolTip(action.getMessage(), action.getShortCut()));
    }

    /**
     * Generate tooltip string for action: use message as tooltip base and add an shortcut mnemonic if any.
     *
     * @param toolTip  tooltip text.
     * @param shortCut keyboard surtcut for the action.
     * @return tool tip text.
     */
    private static String getToolTip(Message toolTip, ShortCut shortCut) {
        String toolTipStr = toolTip.get();
        if (shortCut != null && shortCut.getKeyStroke() != null) {
            return toolTipStr + " (" + ShortCutUtils.keyStrokeHint(shortCut.getKeyStroke()) + ')';
        } else {
            return toolTipStr;
        }
    }

    /**
     * Place specified window at the center of the screen.
     *
     * @param window window to center.
     */
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

    /**
     * Center a windows relative to its owner.
     *
     * @param window window to center.
     */
    public static void center(Window window) {
        if (window != null) {
            center(window, window.getOwner());
        }
    }

    /**
     * Place specified window at the center of specified component. If component is not visible or <code>null</code> -
     * window will be placed at the center of the screen.
     *
     * @param window window to be centered
     * @param parent anchor component.
     * @see #centerOnScreen(java.awt.Window)
     */
    public static void center(Window window, Component parent) {
        if (parent == null || !parent.isVisible()) {
            // Parent is not visible - center window on a screen
            centerOnScreen(window);
            return;
        }

        Window parentWindow = parent instanceof Window ? (Window) parent : SwingUtilities.getWindowAncestor(parent);
        Rectangle screenSize = parentWindow.getGraphicsConfiguration().getBounds();

        Dimension size = parent.getSize();
        Point location = parent.getLocationOnScreen();

        int w = window.getWidth();
        int h = window.getHeight();

        // center according to parent

        int x = location.x + (size.width - w) / 2;
        int y = location.y + (size.height - h) / 2;

        // does it fit on screen?

        if (x < screenSize.x) {
            x = screenSize.x;
        } else if (x + w > screenSize.width + screenSize.x) {
            x = screenSize.x + screenSize.width - w;
        }

        if (y < screenSize.y) {
            y = screenSize.y;
        } else if (y + h > screenSize.height + screenSize.y) {
            y = screenSize.y + screenSize.height - h;
        }

        // done
        window.setBounds(x, y, w, h);
    }

    /**
     * Creates a toolbar and fills it with specified components. If component is <code>null</code> a separator will be
     * placed.
     *
     * @param components list of components to be placed into a new toolbar.
     * @return a toolbar with specified components.
     */
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
     * @return container with constructed buttons.
     */
    public static Component createButtonsBar(JDialog dlg, Message defAction, AButtonAction... buttons) {
        return createButtonsBar(dlg, defAction, FlowLayout.CENTER, buttons);
    }

    /**
     * Constructs a container for buttons and fill the container with the buttons. Also sets up a dialog with default
     * button. Default button is determined by its message. Specified alignment for buttons is used.
     *
     * @param dlg       dialog to be set with default button.
     * @param defAction message of default button.
     * @param align     alignment of buttons in the container.
     * @param buttons   button properties to be placed to buttons container.
     * @return container with constructed buttons.
     */
    public static Component createButtonsBar(JDialog dlg, Message defAction, int align, AButtonAction... buttons) {
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

    public static JButton registerImageButton(JComponent comp, String buttonName, AButtonAction action) {
        registerAction(comp, action);

        return setupImageButton(buttonName, action);
    }

    public static void registerAction(JComponent comp, AButtonAction action) {
        ShortCut sc = action.getShortCut();
        if (sc != null) {
            comp.getInputMap(sc.getCondition()).put(sc.getKeyStroke(), sc);
            comp.getActionMap().put(sc, action);
        }
    }

    public static JPanel createColumn(JComponent... components) {
        return createColumn(0, 0, components);
    }

    public static JPanel createRow(JComponent... components) {
        return createRow(0, 0, components);
    }

    public static JPanel createColumn(int hgap, int vgap, JComponent... components) {
        return createGrid(true, hgap, vgap, components);
    }

    public static JPanel createRow(int hgap, int vgap, JComponent... components) {
        return createGrid(false, hgap, vgap, components);
    }

    private static JPanel createGrid(boolean vertical, int hgap, int vgap, JComponent... components) {
        JPanel panel = new JPanel(new GridLayout(vertical ? 0 : 1, vertical ? 1 : 0, hgap, vgap));

        for (JComponent c : components) {
            panel.add(c);
        }

        return panel;
    }

    public static void toFront(final Frame mainFrame) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainFrame.setVisible(true);
                int state = mainFrame.getExtendedState();
                state &= ~JFrame.ICONIFIED;
                mainFrame.setExtendedState(state);
                mainFrame.setAlwaysOnTop(true);
                mainFrame.toFront();
                mainFrame.requestFocus();
                mainFrame.setAlwaysOnTop(false);
            }
        });
    }

    public static JButton balloonTipCloseButton(final Runnable onClose) {
        JButton closeButton = setupImageButton("cancel", new AButtonAction(Message.Button_Close) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onClose != null) {
                    onClose.run();
                }
            }
        });
        closeButton.setBorder(null);
        closeButton.setContentAreaFilled(false);
        return closeButton;
    }
}
